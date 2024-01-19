package winside.gmonnet.bankaccountkata.application;

import winside.gmonnet.bankaccountkata.domain.entities.Operation;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AccountApi(UUID id, String firstname, String lastname, Instant datetime, double balance, List<Operation> operations) {
}
