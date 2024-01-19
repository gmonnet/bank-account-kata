package winside.gmonnet.bankaccountkata.domain.use_cases;

import winside.gmonnet.bankaccountkata.domain.entities.Operation;

public interface OperationPersistance {

    void saveOperation(Operation operation);
}
