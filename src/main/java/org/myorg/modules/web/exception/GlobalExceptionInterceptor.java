package org.myorg.modules.web.exception;

import org.myorg.modules.exception.ModuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionInterceptor.class);

    @ExceptionHandler(value = { ModuleException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String moduleException(Exception ex) {
        log.info("Exception: ", ex);
        return ex.getMessage();
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalServerError(Exception ex) {
        ModuleException moduleEx = findModuleExceptionIfExist(ex.getCause());
        if (moduleEx != null) {
            return moduleException(moduleEx);
        }
        log.info("Bad exception: ", ex);
        return "Bad exception : " + ex.getMessage();
    }

    private ModuleException findModuleExceptionIfExist(Throwable ex) {
        while (ex != null) {
            if (ex instanceof ModuleException) {
                return (ModuleException) ex;
            }

            ex = ex.getCause();
        }

        return null;
    }

}
