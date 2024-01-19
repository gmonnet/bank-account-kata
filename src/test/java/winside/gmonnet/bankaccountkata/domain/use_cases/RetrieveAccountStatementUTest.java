package winside.gmonnet.bankaccountkata.domain.use_cases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import winside.gmonnet.bankaccountkata.domain.entities.Account;
import winside.gmonnet.bankaccountkata.domain.entities.exception.UnknownAccountException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetrieveAccountStatementUTest {
    @InjectMocks
    RetrieveAccountStatement retrieveAccountStatement;
    @Mock
    AccountPersistance accountPersistance;

    @Test
    void should_throw_an_error_if_account_unknown() {
        // Given
        UUID id = UUID.randomUUID();
        when(accountPersistance.findAccount(id)).thenReturn(Optional.empty());

        // When Then
        assertThatThrownBy(() -> retrieveAccountStatement.execute(id))
                .isInstanceOf(UnknownAccountException.class)
                .hasMessage(UnknownAccountException.MESSAGE);
        verify(accountPersistance, times(1)).findAccount(id);
    }

    @Test
    void should_get_the_account_by_id() {
        // Given
        Account account = new Account("John", "DOE");
        when(accountPersistance.findAccount(account.id())).thenReturn(Optional.of(account));

        // When
        Account result = retrieveAccountStatement.execute(account.id());

        // Then
        assertThat(result).isNotNull().isEqualTo(account);
        verify(accountPersistance, times(1)).findAccount(account.id());
    }
}