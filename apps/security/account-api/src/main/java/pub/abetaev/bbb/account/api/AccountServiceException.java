package pub.abetaev.bbb.account.api;

public class AccountServiceException extends Exception {

  protected AccountServiceException() {
  }

  protected AccountServiceException(String message) {
    super(message);
  }

  protected AccountServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  protected AccountServiceException(Throwable cause) {
    super(cause);
  }

  protected AccountServiceException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
