package winside.gmonnet.bankaccountkata.domain.use_cases;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import winside.gmonnet.bankaccountkata.domain.entities.Account;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CreateAccount {
    private final AccountPersistance accountPersistance;

    public UUID execute(String firstName, String lastName) {
        Account account = new Account(firstName, lastName);
        accountPersistance.saveAccount(account);
        return account.id();
    }
}
