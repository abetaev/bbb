package pub.abetaev.bbb.account.api;

public class AccountNotFoundException extends AccountServiceException {

  public AccountNotFoundException(String id) {
    super(
        String.format("account not found, id='%s'", id)
    );
  }

}
