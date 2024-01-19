package winside.gmonnet.bankaccountkata.infrastructure.database.operation;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import winside.gmonnet.bankaccountkata.domain.entities.Operation;
import winside.gmonnet.bankaccountkata.infrastructure.database.account.AccountDatabase;

@Mapper(componentModel = "spring")
public interface OperationDatabaseMapper {
    @Mapping(target = "accountId", source = "account.id")
    Operation toDomain(OperationDatabase operation);

    @Mapping(target = ".", source = "operation")
    OperationDatabase toInfrastructure(Operation operation);

    @AfterMapping
    default void updateEntity(Operation operation, @MappingTarget OperationDatabase operationDatabase) {
        operationDatabase.setAccount(new AccountDatabase(operation.accountId(), null, null, null, null));
    }
}
