package winside.gmonnet.bankaccountkata.domain.use_cases;

import winside.gmonnet.bankaccountkata.domain.entities.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountPersistance {

    void saveAccount(Account account);
    Optional<Account> findAccount(UUID id);
}
