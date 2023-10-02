package com.swimmingpool.pool.request;

import com.swimmingpool.common.dto.PageRequest;
import lombok.Data;

@Data
public class PoolSearchRequest extends PageRequest {
    private String codeName;
}
