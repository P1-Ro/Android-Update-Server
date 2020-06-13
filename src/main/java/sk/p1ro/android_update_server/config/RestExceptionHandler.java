package sk.p1ro.android_update_server.config;

import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sk.p1ro.android_update_server.util.ErrorResponse;
import sk.p1ro.android_update_server.util.exception.GenericException;
import sk.p1ro.android_update_server.util.exception.NotFoundException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleCustomException(final GenericException ge) {
        return new ErrorResponse(ge.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(final NotFoundException e) {
        return "redirect:/404/";
    }

}
