package winside.gmonnet.bankaccountkata.infrastructure.database.operation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import winside.gmonnet.bankaccountkata.infrastructure.database.account.AccountDatabase;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "operation")
public class OperationDatabase {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountDatabase account;
    private String operationType;
    private double amount;
    private Instant datetime;
}
