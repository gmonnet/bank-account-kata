package winside.gmonnet.bankaccountkata.infrastructure.database.account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import winside.gmonnet.bankaccountkata.domain.entities.Account;
import winside.gmonnet.bankaccountkata.infrastructure.database.operation.OperationDatabaseMapper;

@Mapper(uses = OperationDatabaseMapper.class, componentModel = "spring")
public interface AccountDatabaseMapper {
    @Mapping(target = ".", source = "account")
    AccountDatabase toInfrastructure(Account account);

    Account toDomain(AccountDatabase account);
}
