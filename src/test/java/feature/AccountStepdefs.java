package feature;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.cucumber.java8.StepDefinitionBody;
import org.springframework.beans.factory.annotation.Value;
import winside.gmonnet.bankaccountkata.application.AccountCreationApi;
import winside.gmonnet.bankaccountkata.domain.entities.OperationType;
import winside.gmonnet.bankaccountkata.infrastructure.database.account.AccountDatabase;
import winside.gmonnet.bankaccountkata.infrastructure.database.account.AccountRepository;
import winside.gmonnet.bankaccountkata.infrastructure.database.operation.OperationDatabase;
import winside.gmonnet.bankaccountkata.infrastructure.database.operation.OperationRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class AccountStepdefs implements En {
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${local.server.port}")
    private int port;
    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;
    private HttpResponse<String> response;

    public AccountStepdefs(OperationRepository operationRepository, AccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;

        Given("an empty database", () -> {
            this.operationRepository.deleteAll();
            this.accountRepository.deleteAll();
        });
        When("the user create an account {} {}", (String fistname, String lastname) -> {
            HttpRequest requete = HttpRequest.newBuilder()
                    .uri(new URI(String.format("http://localhost:%d/api/v1/account", port)))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(new AccountCreationApi(fistname, lastname))))
                    .build();
            response = httpClient.send(requete, HttpResponse.BodyHandlers.ofString());
        });
        Then("a http code {} is received", (Integer code) -> {
            assertThat(response).isNotNull();
            assertThat(response.statusCode()).isEqualTo(code);
        });
        Then("the account {} {} exist", (String firstname, String lastname) -> {
            Optional<AccountDatabase> optAccount = this.accountRepository.findAll().stream()
                    .filter(account -> firstname.equals(account.getFirstname()) && lastname.equals(account.getLastname()))
                    .findFirst();
            assertThat(optAccount).isPresent();
        });
        And("an account {} {} exist", (String firstname, String lastname) -> {
            this.accountRepository.save(new AccountDatabase(UUID.randomUUID(), firstname, lastname, Instant.now(), List.of()));
        });
        When("the user deposit {} on the account {} {}", (Integer amount, String firstname, String lastname) -> {
            Optional<AccountDatabase> optAccount = this.accountRepository.findAll().stream()
                    .filter(account -> firstname.equals(account.getFirstname()) && lastname.equals(account.getLastname()))
                    .findFirst();
            assertThat(optAccount).isPresent();
            AccountDatabase accountDatabase = optAccount.get();
            HttpRequest requete = HttpRequest.newBuilder()
                    .uri(new URI(String.format("http://localhost:%d/api/v1/account/%s/deposit", port, accountDatabase.getId())))
                    .header("content-type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(String.valueOf(amount)))
                    .build();
            response = httpClient.send(requete, HttpResponse.BodyHandlers.ofString());
        });
        Then("the account {} {} has a balance of {}", (String firstname, String lastname, Double expectedBalance) -> {
            UUID uuid = this.accountRepository.findAll().stream()
                    .filter(account -> firstname.equals(account.getFirstname()) && lastname.equals(account.getLastname()))
                    .findFirst().map(AccountDatabase::getId).orElseThrow();
            List<OperationDatabase> operations = this.operationRepository.findAll().stream().filter(operationDatabase -> uuid.equals(operationDatabase.getAccount().getId())).toList();

            double deposited = operations.stream()
                    .filter(operationDatabase -> operationDatabase.getOperationType().equals(OperationType.DEPOSIT.name()))
                    .mapToDouble(OperationDatabase::getAmount)
                    .sum();
            double withdrawn = operations.stream()
                    .filter(operationDatabase -> operationDatabase.getOperationType().equals(OperationType.WITHDRAWAL.name()))
                    .mapToDouble(OperationDatabase::getAmount)
                    .sum();
            assertThat(deposited - withdrawn).isEqualTo(expectedBalance);
        });
        When("the user withdraw {} on the account {} {}", (Integer amount, String firstname, String lastname) -> {
            Optional<AccountDatabase> optAccount = this.accountRepository.findAll().stream()
                    .filter(account -> firstname.equals(account.getFirstname()) && lastname.equals(account.getLastname()))
                    .findFirst();
            assertThat(optAccount).isPresent();
            AccountDatabase accountDatabase = optAccount.get();
            HttpRequest requete = HttpRequest.newBuilder()
                    .uri(new URI(String.format("http://localhost:%d/api/v1/account/%s/withdraw", port, accountDatabase.getId())))
                    .header("content-type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(String.valueOf(amount)))
                    .build();
            response = httpClient.send(requete, HttpResponse.BodyHandlers.ofString());
        });
    }
}
