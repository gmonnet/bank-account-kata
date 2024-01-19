package feature;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import winside.gmonnet.bankaccountkata.BankAccountKataApplication;

@CucumberContextConfiguration
@SpringBootTest(
        classes = BankAccountKataApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberConfiguration {}
