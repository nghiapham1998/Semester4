package com.example.projectclient.Service;


import com.example.projectclient.Models.Gmail;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
public class GmailService {

    public List<Gmail> receiveEmail(String pop3Host,
                                    String mailStoreType, String userName, String password) {
        //Set properties
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", pop3Host);
        props.put("mail.imap.port", "995");
        props.put("mail.imap.starttls.enable", "true");

        // Get the Session object.
        Session session = Session.getInstance(props);

        try {
            //Create the POP3 store object and connect to the pop store.
            Store store = session.getStore("imaps");
            store.connect(pop3Host, userName, password);

            //Create the folder object and open it in your mailbox.
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            //Retrieve the messages from the folder object.
            Message[] messages = emailFolder.getMessages();
            System.out.println("Total Message" + messages.length);
            List<Gmail> gmails = new ArrayList<>();

            //Iterate the messages
            for (int i = 0; i < 10; i++) {
                Message message = messages[i];
                Address[] toAddress =
                        message.getRecipients(Message.RecipientType.TO);
                Gmail g = new Gmail();
                g.setDetails(i + 1);
                g.setFrom(message.getFrom()[0].toString());
                g.setSubject(message.getSubject());
                g.setReceivedDate(message.getSentDate());

                for (int j = 0; j < toAddress.length; j++) {
                    g.setTo(toAddress[j].toString());
                }

                gmails.add(g);
            }
            Collections.sort(gmails, new Comparator<Gmail>() {
                @Override
                public int compare(Gmail o1, Gmail o2) {
                    return o2.getReceivedDate().compareTo(o1.getReceivedDate());
                }
            });
            System.out.println(gmails);
            //close the folder and store objects
            emailFolder.close(false);
            store.close();
            return gmails;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean sendMail(Gmail gmail) {

        final String username = "nghiapham1998000@gmail.com";//change accordingly
        final String password = "llmulgvtppkeczds";//change accordingly
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("nghiapham1998000@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(gmail.getTo())
            );
            message.setSubject(gmail.getSubject());
            message.setText(gmail.getBodyEmail());

            Transport.send(message);


            System.out.println("Done");
             return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return false;
    }


}






