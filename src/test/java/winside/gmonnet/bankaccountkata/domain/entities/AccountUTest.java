package winside.gmonnet.bankaccountkata.domain.entities;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountUTest {

    @Test
    void balance_should_return_zero_for_an_account_without_operations() {
        // Given
        Account account = new Account(UUID.randomUUID(), "John", "DOE", Instant.now(), List.of());

        // When
        double balance = account.balance();

        // Then
        assertThat(balance).isZero();
    }

    @Test
    void balance_should_return_sum_of_all_operations_in_an_account() {
        // Given
        UUID id = UUID.randomUUID();
        List<Operation> operations = List.of(
                new Operation(id, OperationType.DEPOSIT, 50),
                new Operation(id, OperationType.DEPOSIT, 30),
                new Operation(id, OperationType.WITHDRAWAL, 60),
                new Operation(id, OperationType.DEPOSIT, 5)
        );
        Account account = new Account(id, "John", "DOE", Instant.now(), operations);

        // When
        double balance = account.balance();

        // Then
        assertThat(balance).isEqualTo(25);
    }

}