package winside.gmonnet.bankaccountkata.domain.entities.exception;

public class InvalidAmountException extends RuntimeException {
    public static final String MESSAGE = "Deposit or Withdrawal amount must be a positive number";
    public InvalidAmountException() {
        super(MESSAGE);
    }
}
