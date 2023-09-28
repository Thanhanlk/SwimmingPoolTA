package com.swimmingpool.common.exception;

import com.swimmingpool.common.util.I18nUtil;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class ValidationException extends BusinessException {

    private final Object[] params;
    private final boolean is18N;

    public ValidationException(String message, Object...params) {
        this(message, null, true, params);
    }

    public ValidationException(String message, String redirectUrl, boolean is18N, Object...params) {
        super(message, redirectUrl);
        this.params = params;
        this.is18N = is18N;
    }

    @Override
    public String getMessage() {
        if (this.is18N) return I18nUtil.getMessage(this.getMessage(), this.params);
        return super.getMessage();
    }
}
