package pub.abetaev.bbb.account.api;

public interface AccountCreatorService {

  void create(String id, String password) throws AccountAlreadyExistsException;

}
