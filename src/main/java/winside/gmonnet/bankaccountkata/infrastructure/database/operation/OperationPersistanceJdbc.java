package winside.gmonnet.bankaccountkata.infrastructure.database.operation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import winside.gmonnet.bankaccountkata.domain.entities.Operation;
import winside.gmonnet.bankaccountkata.domain.use_cases.OperationPersistance;

@Service
@AllArgsConstructor
public class OperationPersistanceJdbc implements OperationPersistance {
    private final OperationRepository repository;
    private final OperationDatabaseMapper mapper;
    @Override
    public void saveOperation(Operation operation) {
        repository.save(mapper.toInfrastructure(operation));
    }
}
