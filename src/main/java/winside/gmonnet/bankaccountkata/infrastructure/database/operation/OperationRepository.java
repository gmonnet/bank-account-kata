package winside.gmonnet.bankaccountkata.infrastructure.database.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OperationRepository extends JpaRepository<OperationDatabase, UUID> {
}
