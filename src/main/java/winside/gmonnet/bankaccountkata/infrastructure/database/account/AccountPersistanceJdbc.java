package winside.gmonnet.bankaccountkata.infrastructure.database.account;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import winside.gmonnet.bankaccountkata.domain.entities.Account;
import winside.gmonnet.bankaccountkata.domain.use_cases.AccountPersistance;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountPersistanceJdbc implements AccountPersistance {
    private final AccountRepository repository;
    private final AccountDatabaseMapper mapper;

    @Override
    public void saveAccount(Account account) {
        repository.save(mapper.toInfrastructure(account));
    }

    @Override
    public Optional<Account> findAccount(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}
