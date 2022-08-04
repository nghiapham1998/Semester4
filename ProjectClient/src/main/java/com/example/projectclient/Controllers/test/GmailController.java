package com.example.projectclient.Controllers.test;


import com.example.projectclient.Models.Gmail;
import com.example.projectclient.Service.GmailService;
import com.example.projectclient.Service.SearchEmail;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Controller
public class GmailController {

    @Autowired
    private GmailService gmailService;
    @Autowired
    private SearchEmail searchEmail;
    @GetMapping("/Admin/Gmail")
    public String getGmail(Model model){
        String pop3Host = "imap.gmail.com";//change accordingly
        String mailStoreType = "imap";
        final String userName = "nghiapham1998000@gmail.com";//change accordingly
        final String password = "llmulgvtppkeczds";//change accordingly

        //call receiveEmail

        model.addAttribute("gmail",  gmailService.receiveEmail(pop3Host, mailStoreType, userName, password));
        return "Admin/Gmail/Gmail";
    }


    @GetMapping("/Gmail/{keyword}")
    public String readBodyGmail(Model model,@PathVariable String keyword) throws MessagingException {
        String pop3Host = "imap.gmail.com";//change accordingly
        String mailStoreType = "imap";
        final String userName = "nghiapham1998000@gmail.com";//change accordingly
        final String password = "llmulgvtppkeczds";//change accordingly
       var mail = searchEmail.searchEmail(pop3Host,userName,password,keyword);
       model.addAttribute("readMail",mail);
        return  "Admin/Gmail/Read";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    @RequestMapping(value = "/Gmail/download1/{files}", method = RequestMethod.GET)
    public void download1(HttpServletResponse response,@PathVariable String files) throws IOException {
        try {
            File file = ResourceUtils.getFile("user-photos/"+files);
            byte[] data = FileUtils.readFileToByteArray(file);
            // Thiết lập thông tin trả về
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
            response.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value ={ "/Admin/Reply","/Reply/{email}"},method = RequestMethod.GET)
    public String replyEmail(@PathVariable(name = "email", required = false) String email,Model model){
        if (email != null){
            model.addAttribute("email",email);
            model.addAttribute("gmail",new Gmail());
            return "Admin/Gmail/Reply";
        }
        model.addAttribute("gmail",new Gmail());
        return "Admin/Gmail/Reply";
    }


    @PostMapping("/Gmail/SendMail")
    public String sendEmail(Model model,@ModelAttribute("gmail") Gmail gmail){
        if (gmailService.sendMail(gmail)){
            model.addAttribute("message","Send Mail Succesfully");
            return "redirect:/Admin/Gmail";
        }
        System.out.println("fails");
        return "Admin/Gmail/Reply";
    }
}
