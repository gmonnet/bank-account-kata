package winside.gmonnet.bankaccountkata.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import winside.gmonnet.bankaccountkata.domain.entities.exception.InsufficientFundsException;
import winside.gmonnet.bankaccountkata.domain.entities.exception.InvalidAmountException;
import winside.gmonnet.bankaccountkata.domain.entities.exception.UnknownAccountException;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({InvalidAmountException.class, InsufficientFundsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String gererControleMetierException(Exception exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(UnknownAccountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String gererElementInexistantException(UnknownAccountException exception) {
        return exception.getMessage();
    }
}
