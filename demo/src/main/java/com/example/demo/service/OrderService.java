package com.example.demo.service;

import com.example.demo.Email.EmailValidator;
import com.example.demo.Email.IEmailSender;
import com.example.demo.Exception.ApiRequestException;
import com.example.demo.Payload.Request.CreateOrderRequest;
import com.example.demo.domain.*;
import com.example.demo.repo.*;
import com.example.demo.service.CloudBinary.CloudBinaryService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.io.IOUtils;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepo userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderProceesRepository orderProceesRepository;

    @Autowired
    CategoryRepository categoryRepository;



    EmailValidator emailValidator;
    private final IEmailSender emailSender;
    @Value("${javadocfast.cloudinary.folder.product}")
    private String image;

    private Map<String, String> options = new HashMap<>();

    String ipv4 = "172.16.0.173";

    @Autowired
    private CloudBinaryService cloudinaryService;

    public OrderService(IEmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public List<Orders> showAll() {
        List<Orders> list = orderRepository.findAll();
        return list;
    }

    public Orders details(Long id){
        if(!orderRepository.existsById(id)){
            return null;
        }
        return  orderRepository.findById(id).get();
    }

    public Orders create(CreateOrderRequest createOrderRequest) throws IOException, WriterException, MessagingException {
        Orders newOrder = new Orders();
        String token = UUID.randomUUID().toString();

        //check storage
        Category category = categoryRepository.findById(createOrderRequest.getCategory_id()).get();
        if(category.getQuantity() <=0){
            return null;
        }

        Product product;
        //check user have product?
        User user = userRepository.findByUsername(createOrderRequest.getUsername()).get();
        if(productRepository.existsByUser(user)){
            product = productRepository.findProductByUser(user);
            product.setYear(product.getYear() + createOrderRequest.getYear());
        }else{
            //create qr code, product
            Map hintMap = new HashMap();
            BitMatrix bitMatrix = new MultiFormatWriter().encode("http://"+ipv4+":8081/Display/"+user.getUsername(), BarcodeFormat.QR_CODE, 500, 500, hintMap);
            options.put("folder", image);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", new File("D:/demo/Qrcode/qrcode.png").toPath());

            File file = new File("D:/demo/Qrcode/qrcode.png");
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("file",
                    file.getName(), "image/png", IOUtils.toByteArray(input));
            Map result =   cloudinaryService.upload(multipartFile, options);
            String url =  result.get("url").toString();

            product = new Product("Description",createOrderRequest.getFullname(),user.getLinkImage(),"http://"+ipv4+":8081/Display/"+user.getUsername(),url,user, new Date(),0, createOrderRequest.getYear(), token);
        }
        productRepository.save(product);

        newOrder.setProduct(product);
        newOrder.setFullname(createOrderRequest.getFullname());
        newOrder.setAddress(createOrderRequest.getAddress());
        newOrder.setPhone(createOrderRequest.getPhone());
        int price = 0;
        if(createOrderRequest.getYear()==1){
            price = 12;
        }else if(createOrderRequest.getYear()==3){
            price = 27;
        }else if(createOrderRequest.getYear()==10){
            price = 82;
        }
        newOrder.setPrice(price);
        newOrder.setCategory(category);
        newOrder.setUser(user);
        newOrder.setCreatedAt(new Date());
        newOrder.setOrder_process(orderProceesRepository.findById(1L).get());
        orderRepository.save(newOrder);

        //sub storage
        category.setQuantity(category.getQuantity()-1);
        if(category.getQuantity() ==0){

        }
        categoryRepository.save(category);


        String link = "http://localhost:8080/api/order/confirmProduct?token=" + token;
        emailSender.send(
                createOrderRequest.getEmail(),"Payment successfully",
                buildEmail(createOrderRequest.getEmail(), link));
        return newOrder;
    }

    public Boolean nextProcess(Long id) throws MessagingException {
        Orders order = orderRepository.findById(id).get();
        Long idProcess = order.getOrder_process().getId();
        if( idProcess < 4 ){
            order.setOrder_process(orderProceesRepository.findById(idProcess + 1L).get());
            if(order.getOrder_process().getId() ==2){
                order.setConfirmedAt(new Date());
                String link = "http://localhost:8080/api/order/confirmOrder?id=" + id ;
                emailSender.send(
                        order.getUser().getEmail(),"Active your Product account",
                        buildEmailSuccess( order.getUser().getEmail(), link));
            }
            if(order.getOrder_process().getId()==3){
                order.setFinishedAt(new Date());
                Product product = order.getProduct();
                product.setStatus(1);
                productRepository.save(product);
            }
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    public Boolean cancelOrder(Long id) throws MessagingException {
        Orders order = orderRepository.findById(id).get();
        if(order != null){
            order.setOrder_process(orderProceesRepository.findById(4L).get());
            order.setCanceledAt(new Date());
            //check user have product?
            User user = order.getUser();
            Product product = productRepository.findProductByUser(user);
            int year = 0;
            if(order.getPrice()==12){
                year = 1;
            }
            if(order.getPrice()==27){
                year = 2;
            }
            if(order.getPrice()==82){
                year = 3;
            }
            if(product.getYear() - year>0){
                product.setYear(product.getYear() - year);
            }else{
                product.setDelete_at(new Date());
            }
            productRepository.save(product);

            //add quantity back
            Category category = orderRepository.getById(id).getCategory();
            category.setQuantity(category.getQuantity()+1);
            categoryRepository.save(category);

            //send mail
            emailSender.send(
                    order.getUser().getEmail(),"Cancel Order",
                    cancelEmail( order.getUser().getEmail(), null));

            orderRepository.save(order);
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public RedirectView confirmToken(Long id)  {

        Orders orders = orderRepository.getById(id);
        orders.setFinishedAt(new Date());

        orders.setOrder_process(orderProceesRepository.findById(3L).get());
        orderRepository.save(orders);

        Product product = orders.getProduct();
        product.setStatus(1);
        productRepository.save(product);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8081/Reviews-"+orders.getCategory().getId());
        return redirectView;
    }
    private int setConfirmedAt(String token) {
        return productRepository.updateConfirmedAt(token);
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
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Payment successfully</span>\n" +
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for your order. Please wait for the administrator to check, and delivery your product. </p>" +
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

    private String cancelEmail(String name, String link) {
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
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Cancel Order</span>\n" +
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Your order has been canceled. If you want to get more information, please contact us by reply this email </p>" +
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


    private String buildEmailSuccess(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Finish your Order</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "             \n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Your Order is on the way. Please click on the link below when you get your order to finish your order: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Comment Now</a> </p></blockquote>\n <p>Thank you and See you soon</p>" +
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
