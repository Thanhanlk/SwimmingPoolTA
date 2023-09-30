package com.swimmingpool.image.response;

import com.swimmingpool.image.ImageConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {
    private String id;
    private String url;
    private String objectId;
    private ImageConstant.Type type;
}
