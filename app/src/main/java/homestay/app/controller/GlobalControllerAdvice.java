package homestay.app.controller;

import homestay.module.utils.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    Response<StackTraceElement[]> handleControllerException(Exception e){
        return new Response<>(4004, e.getStackTrace());
    }
}
