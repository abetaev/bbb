package pub.abetaev.bbb.account.api;

public interface AccountProviderService {

  Account get(String id) throws AccountNotFoundException;

}
