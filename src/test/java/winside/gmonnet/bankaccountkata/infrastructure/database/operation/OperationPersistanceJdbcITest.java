package winside.gmonnet.bankaccountkata.infrastructure.database.operation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import winside.gmonnet.bankaccountkata.domain.entities.Operation;
import winside.gmonnet.bankaccountkata.domain.entities.OperationType;
import winside.gmonnet.bankaccountkata.infrastructure.database.account.AccountDatabase;
import winside.gmonnet.bankaccountkata.infrastructure.database.account.AccountRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class OperationPersistanceJdbcITest {
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected OperationRepository operationRepository;
    @Autowired
    protected OperationPersistanceJdbc operationPersistanceJdbc;

    @Test
    void saveOperation_should_save_an_operation() {
        // Given
        AccountDatabase account = new AccountDatabase(UUID.randomUUID(), "John", "Doe", Instant.now(), List.of());
        accountRepository.save(account);
        Operation operation = new Operation(account.getId(), OperationType.DEPOSIT, 16);

        // When
        operationPersistanceJdbc.saveOperation(operation);

        // Then
        Optional<OperationDatabase> optOperationDatabase = operationRepository.findById(operation.id());
        assertThat(optOperationDatabase).isPresent();
        OperationDatabase operationDatabase = optOperationDatabase.get();
        assertThat(operationDatabase)
                .usingRecursiveComparison()
                .ignoringFields("account", "operationType")
                .isEqualTo(operation);
        assertThat(operationDatabase.getAccount().getId()).isEqualTo(account.getId());
        assertThat(operationDatabase.getOperationType()).isEqualTo(OperationType.DEPOSIT.name());
    }
}