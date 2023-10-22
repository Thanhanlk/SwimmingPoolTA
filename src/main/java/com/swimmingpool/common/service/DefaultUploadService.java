package com.swimmingpool.common.service;

import com.swimmingpool.image.ImageConstant;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DefaultUploadService implements IUploadService {

    @Override
    public String upload(MultipartFile fileItem, String fileName, ImageConstant.Type imageType) throws IOException {
        return "no uploader found";
    }

    @Override
    public String handleAfterUpload(String url) {
        return url;
    }
}
