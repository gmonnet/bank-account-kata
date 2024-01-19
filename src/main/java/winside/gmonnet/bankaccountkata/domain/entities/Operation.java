package winside.gmonnet.bankaccountkata.domain.entities;

import java.time.Instant;
import java.util.UUID;

public record Operation(UUID id, UUID accountId, OperationType operationType, double amount, Instant datetime) {
    public Operation(UUID accountId, OperationType operationType, double amount) {
        this(UUID.randomUUID(), accountId, operationType, amount, Instant.now());
    }
}
