package com.swimmingpool.common.service;

import com.swimmingpool.image.ImageConstant;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface IUploadService {
    default boolean validateBeforeUpload(MultipartFile fileItem) {
        return Optional.ofNullable(fileItem)
                .filter(x -> x.getSize() > 0)
                .isPresent();
    }

    default String uploadFile(MultipartFile file, String fileName, ImageConstant.Type imageType) throws IOException {
        if (!this.validateBeforeUpload(file)) return "";
        String upload = this.upload(file, fileName, imageType);
        return handleAfterUpload(upload);
    }

    String upload(MultipartFile fileItem, String fileName, ImageConstant.Type imageType) throws IOException;

    String handleAfterUpload(String url);
}
