package winside.gmonnet.bankaccountkata.domain.use_cases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import winside.gmonnet.bankaccountkata.domain.entities.Account;
import winside.gmonnet.bankaccountkata.domain.entities.Operation;
import winside.gmonnet.bankaccountkata.domain.entities.OperationType;
import winside.gmonnet.bankaccountkata.domain.entities.exception.InsufficientFundsException;
import winside.gmonnet.bankaccountkata.domain.entities.exception.InvalidAmountException;
import winside.gmonnet.bankaccountkata.domain.entities.exception.UnknownAccountException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MakeAWithdrawalUTest {
    @InjectMocks
    MakeAWithdrawal makeAWithdrawal;
    @Mock
    AccountPersistance accountPersistance;
    @Mock
    OperationPersistance operationPersistance;

    @Captor
    ArgumentCaptor<Operation> operationCaptor;


    @Test
    void should_throw_an_error_when_withdrawn_amount_is_zero() {
        // Given
        double amount = 0;
        Account account = new Account("John", "DOE");
        // When Then
        assertThatThrownBy(() -> makeAWithdrawal.execute(account.id(), amount))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage(InvalidAmountException.MESSAGE);
        verifyNoInteractions(accountPersistance);
        verifyNoInteractions(operationPersistance);
    }

    @Test
    void should_throw_an_error_when_withdrawn_amount_is_negative() {
        // Given
        double amount = -50;
        Account account = new Account("John", "DOE");
        // When Then
        assertThatThrownBy(() -> makeAWithdrawal.execute(account.id(), amount))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage(InvalidAmountException.MESSAGE);
        verifyNoInteractions(accountPersistance);
        verifyNoInteractions(operationPersistance);
    }

    @Test
    void should_throw_an_error_when_account_does_not_exist() {
        // Given
        double amount = 50;
        UUID id = UUID.randomUUID();
        when(accountPersistance.findAccount(id)).thenReturn(Optional.empty());

        // When Then
        assertThatThrownBy(() -> makeAWithdrawal.execute(id, amount))
                .isInstanceOf(UnknownAccountException.class)
                .hasMessage(UnknownAccountException.MESSAGE);
        verify(accountPersistance, times(1)).findAccount(id);
        verifyNoInteractions(operationPersistance);
    }

    @Test
    void should_throw_an_error_when_account_has_insufficient_balance_for_withdrawn_amout() {
        // Given
        double amount = 50;
        UUID id = UUID.randomUUID();
        Account account = new Account(id, "John", "DOE", Instant.now(), List.of(
                new Operation(null, OperationType.DEPOSIT, 30)
        ));
        when(accountPersistance.findAccount(id)).thenReturn(Optional.of(account));

        // When Then
        assertThatThrownBy(() -> makeAWithdrawal.execute(id, amount))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage(InsufficientFundsException.MESSAGE);
        verify(accountPersistance, times(1)).findAccount(id);
        verifyNoInteractions(operationPersistance);
    }

    @Test
    void should_save_withdrawal_operation() {
        // Given
        double amount = 30;
        UUID id = UUID.randomUUID();
        Account account = new Account(id, "John", "DOE", Instant.now(), List.of(
                new Operation(null, OperationType.DEPOSIT, 50)
        ));
        when(accountPersistance.findAccount(id)).thenReturn(Optional.of(account));

        // When Then
        makeAWithdrawal.execute(id, amount);
        verify(accountPersistance, times(1)).findAccount(id);
        verify(operationPersistance, times(1)).saveOperation(operationCaptor.capture());

        Operation savedOperation = operationCaptor.getValue();
        assertThat(savedOperation).isNotNull();
        assertThat(savedOperation.id()).isNotNull();
        assertThat(savedOperation.accountId()).isEqualTo(id);
        assertThat(savedOperation.amount()).isEqualTo(amount);
        assertThat(savedOperation.operationType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(savedOperation.datetime()).isCloseTo(Instant.now(), within(500, ChronoUnit.MILLIS));
    }
}