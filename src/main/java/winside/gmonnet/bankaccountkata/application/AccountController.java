package winside.gmonnet.bankaccountkata.application;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import winside.gmonnet.bankaccountkata.domain.use_cases.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private CreateAccount createAccount;
    private MakeADeposit makeADeposit;
    private MakeAWithdrawal makeAWithdrawal;
    private RetrieveAccountStatement retrieveAccountStatement;
    private RetrieveOperationsHistory retrieveOperationsHistory;
    private AccountApiMapper accountApiMapper;
    private OperationApiMapper operationApiMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createNewAccount(@RequestBody AccountCreationApi accountCreationApi) {
        return createAccount.execute(accountCreationApi.firstname(), accountCreationApi.lastname());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountApi findAccount(@PathVariable("id") UUID id) {
        return accountApiMapper.toAPi(retrieveAccountStatement.execute(id));
    }

    @GetMapping("/{id}/operations")
    @ResponseStatus(HttpStatus.OK)
    public List<OperationApi> listOperations(@PathVariable("id") UUID id) {
        return retrieveOperationsHistory.execute(id).stream().map(operationApiMapper::toApi).toList();
    }

    @PutMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void deposit(@PathVariable("id") UUID id, @RequestBody double amount) {
        makeADeposit.execute(id, amount);
    }

    @PutMapping("/{id}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@PathVariable("id") UUID id, @RequestBody double amount) {
        makeAWithdrawal.execute(id, amount);
    }
}
