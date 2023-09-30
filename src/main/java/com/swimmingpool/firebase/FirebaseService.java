package com.swimmingpool.firebase;

import com.google.cloud.storage.Bucket;
import com.swimmingpool.common.service.IUploadService;
import com.swimmingpool.image.ImageConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@ConditionalOnClass(FirebaseProperties.class)
@Slf4j
@Primary
public class FirebaseService implements IUploadService {

    private final Bucket bucket;
    private final FirebaseProperties firebaseProperties;

    @Override
    public String upload(MultipartFile file, String fileName, ImageConstant.Type imageType) throws IOException {
        String name = String.format("%s%s%s", imageType.getPath(), fileName, StringUtils.getFilenameExtension(file.getOriginalFilename()));
        bucket.create(name, file.getInputStream(), file.getContentType());
        return name;
    }

    @Override
    public String handleAfterUpload(String url) {
        String encode = URLEncoder.encode(url, StandardCharsets.UTF_8);
        return MessageFormat.format(firebaseProperties.getUrlImage(), this.bucket.getName(), encode);
    }
}
