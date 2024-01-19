package winside.gmonnet.bankaccountkata.application;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import winside.gmonnet.bankaccountkata.domain.entities.Operation;
import winside.gmonnet.bankaccountkata.infrastructure.database.account.AccountDatabase;
import winside.gmonnet.bankaccountkata.infrastructure.database.operation.OperationDatabase;

@Mapper(componentModel = "spring")
public interface OperationApiMapper {
    @Mapping(target = ".", source = "operation")
    OperationApi toApi(Operation operation);
}
