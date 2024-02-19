package project.kidscafe.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.kidscafe.api.ApiResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({BindException.class})
    public ApiResponse<String> bindException(BindException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.of(BAD_REQUEST, e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public ApiResponse<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        if (e.getClass().equals(CustomException.class)) {
            CustomException customException = (CustomException) e;
            return ApiResponse.of(customException.getErrorCode().getStatus(), customException.getMessage());
        }
        return ApiResponse.of(INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
