package winside.gmonnet.bankaccountkata.domain.use_cases;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import winside.gmonnet.bankaccountkata.domain.entities.Account;
import winside.gmonnet.bankaccountkata.domain.entities.exception.UnknownAccountException;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RetrieveAccountStatement {
    private final AccountPersistance accountPersistance;

    public Account execute(UUID id) {
        return accountPersistance.findAccount(id)
                .orElseThrow(UnknownAccountException::new);
    }
}
