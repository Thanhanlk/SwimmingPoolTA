package com.swimmingpool.image.impl;

import com.swimmingpool.common.service.IUploadService;
import com.swimmingpool.image.Image;
import com.swimmingpool.image.ImageConstant;
import com.swimmingpool.image.ImageRepository;
import com.swimmingpool.image.ImageService;
import com.swimmingpool.image.request.ImageRequest;
import com.swimmingpool.image.response.ImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final IUploadService uploadService;
    private final ImageRepository imageRepository;

    @Override
    public String upload(MultipartFile file, String fileName, ImageConstant.Type type) throws IOException {
        log.info("upload {}/{}", type, fileName);
        return this.uploadService.uploadFile(file, fileName, type);
    }

    @Override
    public ImageResponse saveImage(ImageRequest imageRequest) throws IOException {
        String url = this.upload(imageRequest.getFile(), imageRequest.getFileName(), imageRequest.getType());
        Image image = new Image();
        image.setObjectId(image.getObjectId());
        image.setUrl(url);
        image.setType(imageRequest.getType());
        image = this.imageRepository.save(image);
        return ImageResponse.builder()
                .id(image.getId())
                .objectId(image.getObjectId())
                .url(image.getUrl())
                .type(image.getType())
                .build();
    }
}
