package winside.gmonnet.bankaccountkata.domain.use_cases;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import winside.gmonnet.bankaccountkata.domain.entities.Account;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateAccountUTest {
    @InjectMocks
    CreateAccount createAccount;
    @Mock
    AccountPersistance accountPersistance;

    @Captor
    ArgumentCaptor<Account> accountCaptor;

    @ParameterizedTest
    @CsvSource({
            "SMITH,John",
            "Dupont,Jean",
            "LASTNAME,Firstname",
    })
    void should_create_an_account(String lastName, String firstName) {
        // Given
        // When
        UUID id = createAccount.execute(firstName, lastName);

        // Then
        assertThat(id).isNotNull();
        verify(accountPersistance, times(1)).saveAccount(accountCaptor.capture());
        Account createdAccount = accountCaptor.getValue();
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.id()).isEqualTo(id);
        assertThat(createdAccount.lastname()).isEqualTo(lastName);
        assertThat(createdAccount.firstname()).isEqualTo(firstName);
        assertThat(createdAccount.balance()).isZero();
        assertThat(createdAccount.operations()).isNotNull().isEmpty();
        assertThat(createdAccount.datetime()).isCloseTo(Instant.now(), within(500, ChronoUnit.MILLIS));
    }
}