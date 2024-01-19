package winside.gmonnet.bankaccountkata.domain.entities.exception;

import java.util.UUID;

public class UnknownAccountException extends RuntimeException {
    public static final String MESSAGE = "Unknown account";
    public UnknownAccountException() {
        super(String.format(MESSAGE));
    }
}
