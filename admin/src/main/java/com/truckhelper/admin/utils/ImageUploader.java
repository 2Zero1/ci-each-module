package com.truckhelper.admin.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.github.f4b6a3.ulid.UlidCreator;

@Service
public class ImageUploader {
    private Cloudinary cloudinary;

    private String postfix;

    public ImageUploader(
            Cloudinary cloudinary,
            @Value("${cloudinary.postfix}") String postfix
    ) {
        this.cloudinary = cloudinary;
        this.postfix = postfix;
    }

    public String upload(String folder, MultipartFile file) {
        try {
            String id = UlidCreator.getUlid().toString();
            String folderName = folder + this.postfix;
            String publicId = folderName + "/" + id;

            Map result = cloudinary.uploader()
                    .upload(
                            file.getBytes(),
                            ObjectUtils.asMap("public_id", publicId)
                    );

            return result.get("url").toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
