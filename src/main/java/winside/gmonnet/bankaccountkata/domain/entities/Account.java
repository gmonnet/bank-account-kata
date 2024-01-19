package winside.gmonnet.bankaccountkata.domain.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Account(UUID id, String firstname, String lastname, Instant datetime, List<Operation> operations) {

    public Account(String firstname, String lastname) {
        this(UUID.randomUUID(), firstname, lastname, Instant.now(), List.of());
    }

    public double balance() {
        double deposited = operations.stream()
                .filter(operation -> operation.operationType().equals(OperationType.DEPOSIT))
                .mapToDouble(Operation::amount)
                .sum();
        double withdrawn = operations.stream()
                .filter(operation -> operation.operationType().equals(OperationType.WITHDRAWAL))
                .mapToDouble(Operation::amount)
                .sum();
        return deposited - withdrawn;
    }
}
