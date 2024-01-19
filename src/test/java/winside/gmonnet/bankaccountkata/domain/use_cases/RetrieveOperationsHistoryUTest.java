package winside.gmonnet.bankaccountkata.domain.use_cases;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Component;
import winside.gmonnet.bankaccountkata.domain.entities.Account;
import winside.gmonnet.bankaccountkata.domain.entities.Operation;
import winside.gmonnet.bankaccountkata.domain.entities.OperationType;
import winside.gmonnet.bankaccountkata.domain.entities.exception.UnknownAccountException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RetrieveOperationsHistoryUTest {
    @InjectMocks
    RetrieveOperationsHistory retrieveOperationsHistory;
    @Mock
    AccountPersistance accountPersistance;

    @Test
    void should_throw_an_error_if_account_unknown() {
        // Given
        UUID id = UUID.randomUUID();
        when(accountPersistance.findAccount(id)).thenReturn(Optional.empty());

        // When Then
        assertThatThrownBy(() -> retrieveOperationsHistory.execute(id))
                .isInstanceOf(UnknownAccountException.class)
                .hasMessage(UnknownAccountException.MESSAGE);
        verify(accountPersistance, times(1)).findAccount(id);
    }

    @Test
    void should_get_an_empty_list_for_an_account_without_operations() {
        // Given
        Account account = new Account("John", "DOE");
        when(accountPersistance.findAccount(account.id())).thenReturn(Optional.of(account));

        // When
        List<Operation> operations = retrieveOperationsHistory.execute(account.id());

        // Then
        assertThat(operations).isNotNull().isEmpty();
        verify(accountPersistance, times(1)).findAccount(account.id());
    }

    @Test
    void should_get_all_operations_from_an_account() {
        // Given
        UUID accountId = UUID.randomUUID();
        List<Operation> storedOperations = List.of(
                new Operation(accountId, OperationType.DEPOSIT, 50),
                new Operation(accountId, OperationType.DEPOSIT, 70),
                new Operation(accountId, OperationType.WITHDRAWAL, 30),
                new Operation(accountId, OperationType.WITHDRAWAL, 10),
                new Operation(accountId, OperationType.WITHDRAWAL, 5),
                new Operation(accountId, OperationType.DEPOSIT, 100),
                new Operation(accountId, OperationType.WITHDRAWAL, 15)
        );
        Account account = new Account(accountId, "John", "DOE", Instant.now(), storedOperations);
        when(accountPersistance.findAccount(accountId)).thenReturn(Optional.of(account));

        // When
        List<Operation> operations = retrieveOperationsHistory.execute(accountId);

        // Then
        assertThat(operations)
                .isNotNull()
                .hasSameSizeAs(storedOperations)
                .containsExactlyElementsOf(storedOperations);
        verify(accountPersistance, times(1)).findAccount(account.id());
    }
}