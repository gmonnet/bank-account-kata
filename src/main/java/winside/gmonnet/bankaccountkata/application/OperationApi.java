package winside.gmonnet.bankaccountkata.application;

import java.time.Instant;
import java.util.UUID;

public record OperationApi(UUID id, UUID accountId, String operationType, double amount, Instant datetime) {
}
