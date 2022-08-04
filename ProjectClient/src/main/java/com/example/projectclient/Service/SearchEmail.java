package com.example.projectclient.Service;

import com.example.projectclient.Models.Gmail;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class SearchEmail {
    public List<Gmail> searchEmail(String host, String userName,
                                   String password, String keyword) throws MessagingException {
        Properties props = new Properties();

        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", "995");
        props.put("mail.imap.starttls.enable", "true");


        Session session = Session.getInstance(props);

        Folder folderInbox = null;
        Store store = null;
        try {
            // connects to the message store
            store = session.getStore("imaps");
            store.connect(host, userName, password);

            // opens the inbox folder
            folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            // creates a search criterion
            SearchTerm searchCondition = new SearchTerm() {
                @Override
                public boolean match(Message message) {
                    try {
                        if (message.getSubject().replace(' ','-').contains(keyword.replace(' ','-'))) {
                            return true;
                        }
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                    return false;
                }
            };

            // performs search through the folder
            Message[] foundMessages = folderInbox.search(searchCondition);
            List<Gmail> gmails = new ArrayList<>();
            for (int i = 0; i < foundMessages.length; i++) {
                Message message = foundMessages[i];
                Gmail g = new Gmail();
                Multipart multipart = (Multipart) message.getContent();
                int numberOrPart = multipart.getCount();

                for (int partCount = 0; partCount < numberOrPart; partCount++) {
                    MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(partCount);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        String file = part.getFileName();


                        System.out.println(file);

                        System.out.println(part.getSize());
                        System.out.println(part.getSize());
                        g.setFileSize(part.getSize());
                        g.setFile(file);
                        Path uploadPath = Paths.get("user-photos/");

                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }

                        try (InputStream inputStream = part.getInputStream()) {
                            Path filePath = uploadPath.resolve(file);
                            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException ioe) {
                            throw new IOException("Could not save image file: " + file, ioe);
                        }
                    }
                }
                String subject = message.getSubject();


                g.setFrom(String.valueOf(message.getFrom()[0]));
                g.setSubject(message.getSubject());
                g.setReceivedDate(message.getSentDate());
                g.setBodyEmail(getTextFromMessage(message));


                gmails.add(g);

                System.out.println("Found message #" + i + ": " + subject);
            }

            // disconnect

            return gmails;
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store.");
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }else if (message.isMimeType("text/html")) {
            String html = (String) message.getContent().toString();
            result = result + "\n" + html;
        }
        return result;
    }

    private static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + html;
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }







}
