package pub.abetaev.bbb.authentication.service;

import pub.abetaev.bbb.account.api.Account;
import pub.abetaev.bbb.account.api.AccountNotFoundException;
import pub.abetaev.bbb.account.api.AccountProviderService;
import pub.abetaev.bbb.authentication.api.AuthenticationToken;
import pub.abetaev.bbb.authentication.api.NotAuthenticatedException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class App {

  public static void main(String... args) {
    SpringApplication.run(App.class, args);
  }

}

@FeignClient("account-service")
interface AccountProviderServiceRemote extends AccountProviderService {

  @GetMapping("/{id}")
  Account get(@PathVariable("id") String id);

}

@RestController
class AuthenticationRestController {

  private final AccountProviderService accountService;

  AuthenticationRestController(AccountProviderService accountProviderService) {
    this.accountService = accountProviderService;
  }

  @PostMapping
  AuthenticationToken authenticate(String accountId, String password)
      throws NotAuthenticatedException {
    Account account;
    try {
      account = accountService.get(accountId);
    } catch (AccountNotFoundException e) {
      throw new NotAuthenticatedException();
    }

    if (Objects.equals(account.getPassword(), password)) {
      return new AuthenticationToken() {};
    }

    throw new NotAuthenticatedException();
  }

}