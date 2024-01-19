package winside.gmonnet.bankaccountkata.infrastructure.database.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import winside.gmonnet.bankaccountkata.infrastructure.database.operation.OperationDatabase;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account")
public class AccountDatabase {
    @Id
    private UUID id;
    private String firstname;
    private String lastname;
    private Instant datetime;
    @OneToMany(mappedBy = "account")
    private List<OperationDatabase> operations;
}
