package winside.gmonnet.bankaccountkata.domain.entities.exception;

public class InsufficientFundsException extends RuntimeException {
    public static final String MESSAGE = "Insufficient funds in the bank account to process withdrawal";

    public InsufficientFundsException() {
        super(MESSAGE);
    }
}
