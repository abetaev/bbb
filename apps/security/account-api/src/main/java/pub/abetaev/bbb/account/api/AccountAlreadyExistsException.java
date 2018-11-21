package pub.abetaev.bbb.account.api;

public class AccountAlreadyExistsException extends AccountServiceException {


  public AccountAlreadyExistsException(String id) {
    super(
        String.format(
            "account already exists, id='%s'", id
        )
    );
  }

}
