package com.example.projectclient.Service;

import com.example.projectclient.Service.CloudBinary.CloudBinaryService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;


@Service
public class PdfService {
    @Autowired
    CloudBinaryService cloudBinaryService;
    private Map<String, String> options = new HashMap<>();
    @Value("${javadocfast.cloudinary.folder.product}")
    private String image;
    public void Create(Long id,String name, String productImage) throws IOException {
        // tạo một document
        Document document = new Document();
        try {
            // khởi tạo một PdfWriter truyền vào document và FileOutputStream
            PdfWriter.getInstance(document, new FileOutputStream("D:/ProjectClient/src/main/resources/templates/Admin/product-pdf/" +id +".pdf"));
            // mở file để thực hiện viết
            document.open();
            // thêm nội dung sử dụng add function

            //add Paragrap
            Paragraph paragraph1 = new Paragraph();
            Image image1 = Image.getInstance("https://res.cloudinary.com/tphcm/image/upload/v1658291711/product/2_x4vppq.png");
            image1.scalePercent(50f);
            paragraph1.add(new Chunk(image1, 0 , -72, true));
            paragraph1.add(new Chunk(name, new Font(Font.FontFamily.HELVETICA, 25, 2,BaseColor.WHITE)));
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            paragraph1.setSpacingAfter(100);

            Paragraph paragraph2 = new Paragraph();
            Image image2 = Image.getInstance("https://res.cloudinary.com/tphcm/image/upload/v1658291711/product/1_se9tbi.png");
            image2.scalePercent(50f);
            paragraph2.add(new Chunk(image2, 10 , -220, true));
            Image image3 = Image.getInstance(productImage);
            image3.scalePercent(27f);
            paragraph2.add(new Chunk(image3, 65 , -150, true));

            document.add(paragraph1);
            document.add(paragraph2);


            // đóng file
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
