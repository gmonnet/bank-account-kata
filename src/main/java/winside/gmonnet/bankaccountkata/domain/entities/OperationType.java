package winside.gmonnet.bankaccountkata.domain.entities;

public enum OperationType {
    WITHDRAWAL("WITHDRAWAL"), DEPOSIT("DEPOSIT");

    private final String operation;

    OperationType(String operation) {
        this.operation = operation;
    }
}
