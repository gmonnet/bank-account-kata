package winside.gmonnet.bankaccountkata.domain.use_cases;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import winside.gmonnet.bankaccountkata.domain.entities.Account;
import winside.gmonnet.bankaccountkata.domain.entities.Operation;
import winside.gmonnet.bankaccountkata.domain.entities.OperationType;
import winside.gmonnet.bankaccountkata.domain.entities.exception.InsufficientFundsException;
import winside.gmonnet.bankaccountkata.domain.entities.exception.InvalidAmountException;
import winside.gmonnet.bankaccountkata.domain.entities.exception.UnknownAccountException;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MakeAWithdrawal {
    private final AccountPersistance accountPersistance;
    private final OperationPersistance operationPersistance;

    public void execute(UUID accountId, double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
        Account account = accountPersistance.findAccount(accountId)
                .orElseThrow(UnknownAccountException::new);
        if (amount > account.balance()) {
            throw new InsufficientFundsException();
        }
        Operation operation = new Operation(accountId, OperationType.WITHDRAWAL, amount);
        operationPersistance.saveOperation(operation);
    }
}
