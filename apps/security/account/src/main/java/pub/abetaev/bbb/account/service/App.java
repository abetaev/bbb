package pub.abetaev.bbb.account.service;


import pub.abetaev.bbb.account.api.Account;
import pub.abetaev.bbb.account.api.AccountAlreadyExistsException;
import pub.abetaev.bbb.account.api.AccountCreatorService;
import pub.abetaev.bbb.account.api.AccountNotFoundException;
import pub.abetaev.bbb.account.api.AccountProviderService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
public class App {

  public static void main(String... args) {

    SpringApplication.run(App.class, args);

  }

  @Bean("accountStorage")
  Map<String, Account> accountStorage() {
    return new HashMap<>();
  }

}


@RestController
@RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE
)
class AccountRestController implements AccountCreatorService, AccountProviderService {

  private final Map<String, Account> storage;

  AccountRestController(@Qualifier("accountStorage") Map<String, Account> storage) {
    this.storage = storage;
  }

  @Override
  @PostMapping
  public void create(String id, String password) throws AccountAlreadyExistsException {
    Account oldAccount = storage.get(id);
    if (oldAccount != null) {
      throw new AccountAlreadyExistsException(id);
    }
    Account newAccount = new Account(id, password);
    storage.put(id, newAccount);
  }

  @Override
  @GetMapping("/{id}" )
  public Account get(@PathVariable("id") String id) throws AccountNotFoundException {
    Account account = storage.get(id);
    if (account == null) {
      throw new AccountNotFoundException(id);
    }
    return account;
  }

}