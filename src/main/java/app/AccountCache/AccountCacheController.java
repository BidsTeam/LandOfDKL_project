package app.AccountCache;


public class AccountCacheController implements AccountCacheControllerMBean {
    private final AccountCache accountServer;

    public AccountCacheController(AccountCache accountServer) {
        this.accountServer = accountServer;
    }

    @Override
    public int getLoggedCounter() {
        return accountServer.getLoggedCounter();
    }

}
