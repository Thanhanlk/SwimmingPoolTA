package com.swimmingpool.image.request;

import com.swimmingpool.image.ImageConstant;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageRequest {
    private MultipartFile file;
    private String fileName;
    private ImageConstant.Type type;
    private String objectId;
}
