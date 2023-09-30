package com.swimmingpool.image;

import com.swimmingpool.image.request.ImageRequest;
import com.swimmingpool.image.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    ImageResponse saveImage(ImageRequest imageRequest) throws IOException;
    String upload(MultipartFile file, String fileName, ImageConstant.Type type) throws IOException;
}
