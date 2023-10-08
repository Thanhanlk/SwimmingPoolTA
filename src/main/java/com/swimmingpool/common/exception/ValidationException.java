package com.swimmingpool.common.exception;

import com.swimmingpool.common.util.I18nUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class ValidationException extends BusinessException {

    private Object[] params;
    private boolean is18N;

    public ValidationException(String message, Object...params) {
        super(message, null);
        this.params = params;
        this.is18N = true;
    }

    @Override
    public String getMessage() {
        if (this.is18N) return I18nUtil.getMessage(super.getMessage(), this.params);
        return super.getMessage();
    }
}
