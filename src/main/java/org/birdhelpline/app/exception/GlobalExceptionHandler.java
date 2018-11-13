package org.birdhelpline.app.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileUploadBase.FileSizeLimitExceededException.class)
    public @ResponseBody String handleMaxUploadException(MaxUploadSizeExceededException e){
        return "Image size too big, should be less then < 10MB";
    }
}
