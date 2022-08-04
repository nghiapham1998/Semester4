package com.example.projectclient.Service.CloudBinary;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudBinaryService {
    private Cloudinary cloudinary;
    private String cloudName = "tphcm";
    private String apiKey = "575123198884825";
    private String apiSecret = "pSPdlR8KIsV5X1Np0o8rcjXQSk8";
    private Map<String, String> valuesMap;

    public CloudBinaryService() {

        valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", cloudName);
        valuesMap.put("api_key", apiKey);
        valuesMap.put("api_secret", apiSecret);
        cloudinary = new Cloudinary(valuesMap);
    }

    public Map upload(MultipartFile multipartFile, Map<String, String> options) throws IOException {
        File file = convert(multipartFile);

        Map result = cloudinary.uploader().upload(file, options);
        file.delete();
        return result;
    }

    public Map delete(String id, Map<String, String> options) throws IOException {
        Map result = null;
        try {
            result = cloudinary.uploader().destroy(id, options);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }

}
