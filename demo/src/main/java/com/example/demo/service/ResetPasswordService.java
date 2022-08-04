package com.example.demo.service;

import com.example.demo.Email.EmailValidator;
import com.example.demo.Email.IEmailSender;
import com.example.demo.Exception.ApiRequestException;
import com.example.demo.Payload.Request.ResetPasswordRequest;
import com.example.demo.domain.ResetPassword;
import com.example.demo.domain.User;
import com.example.demo.repo.ResetPasswordRepo;
import com.example.demo.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResetPasswordService {
    EmailValidator emailValidator;
    private final IEmailSender emailSender;

    private final UserService userService;

   private final  PasswordEncoder  encoder;
    @Autowired
    UserRepo userRepository;

    @Autowired
    ResetPasswordRepo resetPasswordRepo;
    public String changePassword(ResetPasswordRequest request) throws MessagingException {
        boolean isValidEmail = emailValidator.
                test(request.getEmail());

        if (!isValidEmail) {
            throw new ApiRequestException("email not valid");
        }
         boolean user =   userRepository.existsByEmail(request.getEmail());
        if (!user) {
            throw new ApiRequestException("Error: Invalid Email not exist !!!");
        }

        User findbyEmail = userRepository.findByEmail(request.getEmail());

        String token = UUID.randomUUID().toString();
        ResetPassword resetPassword = resetPasswordRepo.save(new ResetPassword(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                findbyEmail
        ));

        String link = "http://localhost:8080/api/auth/confirmPassword?token=" + token;
        emailSender.send(
                request.getEmail(),"Change Password",
                buildEmail(request.getEmail(), link));

        return token;
    }


    private int setConfirmedAt(String token) {
        return resetPasswordRepo.updateConfirmedAt(
                token, LocalDateTime.now());
    }
    @Transactional
    public RedirectView confirmToken(String token)  {
        ResetPassword resetPassword =resetPasswordRepo.findByToken(token)
                .orElseThrow(() ->
                        new ApiRequestException("token not found"));

        if (resetPassword.getConfirmedAt() != null) {
            throw new ApiRequestException("email already confirmed");
        }

        LocalDateTime expiredAt = resetPassword.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ApiRequestException("token expired");
        }

        setConfirmedAt(token);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8081/resetPassword");
        return redirectView;
    }

    public void updatePassword(@Valid ResetPasswordRequest request){
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null){
            throw new ApiRequestException("email do not exist !!");
        }
        String date = user.getDateOfbirth().toString();
        if (user.getUsername().contains(request.getPassword())){
            throw new ApiRequestException("Password The user cannot have a username");
        }
        if (user.getEmail().contains(request.getPassword())){
            throw new ApiRequestException("Password The user cannot have a Email");
        }
        if (user.getPhone().contains(request.getPassword())){
            throw new ApiRequestException("Password The user cannot have a Phone");
        }
        if (date.contains(request.getPassword())){
            throw new ApiRequestException("Password The user cannot have a BirthDay");
        }

        user.setPassword(encoder.encode(request.getPassword()));
        System.out.printf(user.getUsername());
        userService.saveUser(user);
    }



    private String buildEmail(String name, String link) {
        return
                "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
