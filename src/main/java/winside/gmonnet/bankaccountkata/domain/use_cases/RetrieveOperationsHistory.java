package winside.gmonnet.bankaccountkata.domain.use_cases;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import winside.gmonnet.bankaccountkata.domain.entities.Account;
import winside.gmonnet.bankaccountkata.domain.entities.Operation;
import winside.gmonnet.bankaccountkata.domain.entities.exception.UnknownAccountException;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RetrieveOperationsHistory {
    private final AccountPersistance accountPersistance;

    public List<Operation> execute(UUID accountId) {
        Account account = accountPersistance.findAccount(accountId).orElseThrow(UnknownAccountException::new);
        return account.operations();
    }
}
