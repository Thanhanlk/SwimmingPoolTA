package com.swimmingpool.pool;

import com.swimmingpool.pool.response.PoolResponse;
import jakarta.persistence.Tuple;
import lombok.experimental.UtilityClass;

import java.util.Date;
import java.util.function.Function;

@UtilityClass
public class PoolRowMapper {

    public Function<Tuple, PoolResponse> poolSearchRowMapper() {
        return tuple -> {
            PoolResponse p = new PoolResponse();
            p.setId(tuple.get("id", String.class));
            p.setName(tuple.get("name", String.class));
            p.setCode(tuple.get("code", String.class));
            p.setActive(tuple.get("active", Boolean.class));
            p.setCreatedDate(tuple.get("createdDate", Date.class));
            p.setModifiedDate(tuple.get("modifiedDate", Date.class));
            return p;
        };
    }
}
