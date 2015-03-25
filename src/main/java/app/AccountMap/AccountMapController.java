package app.AccountMap;


public class AccountMapController implements AccountMapControllerMBean {
    private final AccountMap accountServer;

    public AccountMapController(AccountMap accountServer) {
        this.accountServer = accountServer;
    }

    @Override
    public int getLoggedCounter() {
        return accountServer.getLoggedCounter();
    }

}
