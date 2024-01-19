package winside.gmonnet.bankaccountkata.infrastructure.database.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import winside.gmonnet.bankaccountkata.domain.entities.Account;
import winside.gmonnet.bankaccountkata.domain.entities.Operation;
import winside.gmonnet.bankaccountkata.domain.entities.OperationType;
import winside.gmonnet.bankaccountkata.infrastructure.database.operation.OperationDatabase;
import winside.gmonnet.bankaccountkata.infrastructure.database.operation.OperationRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class AccountPersistanceJdbcITest {
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected OperationRepository operationRepository;
    @Autowired
    protected AccountPersistanceJdbc accountPersistanceJdbc;

    @Test
    void saveAccount_should_save_an_account() {
        // Given
        Account account = new Account("John", "Doe");

        // When
        accountPersistanceJdbc.saveAccount(account);

        // Then
        Optional<AccountDatabase> optSavedAccount = accountRepository.findById(account.id());
        assertThat(optSavedAccount)
                .isPresent().get()
                .usingRecursiveComparison().isEqualTo(account);
    }

    @Test
    void findAccount_should_return_empty_optional_for_unknown_account() {
        // Given
        UUID accountId = UUID.randomUUID();

        // When
        Optional<Account> optAccount = accountPersistanceJdbc.findAccount(accountId);

        // Then
        assertThat(optAccount).isEmpty();
    }

    @Test
    void findAccount_should_return_an_existing_account_with_its_operations() {
        // Given
        UUID accountId = UUID.randomUUID();
        AccountDatabase accountDatabase = new AccountDatabase(accountId, "John", "DOE", Instant.now(), List.of());
        accountRepository.save(accountDatabase);
        OperationDatabase operationDatabase1 = new OperationDatabase(UUID.randomUUID(), accountDatabase, OperationType.DEPOSIT.name(), 50, Instant.now().minusSeconds(100));
        operationRepository.save(operationDatabase1);
        OperationDatabase operationDatabase2 = new OperationDatabase(UUID.randomUUID(), accountDatabase, OperationType.WITHDRAWAL.name(), 40, Instant.now().minusSeconds(70));
        operationRepository.save(operationDatabase2);
        OperationDatabase operationDatabase3 = new OperationDatabase(UUID.randomUUID(), accountDatabase, OperationType.WITHDRAWAL.name(), 10, Instant.now().minusSeconds(30));
        operationRepository.save(operationDatabase3);
        OperationDatabase operationDatabase4 = new OperationDatabase(UUID.randomUUID(), accountDatabase, OperationType.DEPOSIT.name(), 100, Instant.now());
        operationRepository.save(operationDatabase4);
        accountDatabase.setOperations(List.of(operationDatabase1, operationDatabase2, operationDatabase3, operationDatabase4));
        accountRepository.save(accountDatabase);


        // When
        Optional<Account> optAccount = accountPersistanceJdbc.findAccount(accountId);

        // Then
        assertThat(optAccount).isPresent();
        Account account = optAccount.get();
        assertThat(account)
                .usingRecursiveComparison()
                .ignoringFields("operations")
                .isEqualTo(accountDatabase);
        assertThat(account.operations())
                .hasSize(4)
                .map(Operation::id)
                .containsExactlyInAnyOrder(operationDatabase1.getId(),operationDatabase2.getId(),operationDatabase3.getId(),operationDatabase4.getId());
    }
}