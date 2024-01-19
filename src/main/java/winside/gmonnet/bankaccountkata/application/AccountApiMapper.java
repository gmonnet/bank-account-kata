package winside.gmonnet.bankaccountkata.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import winside.gmonnet.bankaccountkata.domain.entities.Account;
import winside.gmonnet.bankaccountkata.infrastructure.database.account.AccountDatabase;

@Mapper(uses = OperationApiMapper.class, componentModel = "spring")
public interface AccountApiMapper {
    @Mapping(target = ".", source = "account")
    @Mapping(target = "balance", expression = "java(account.balance())")
    AccountApi toAPi(Account account);
}
