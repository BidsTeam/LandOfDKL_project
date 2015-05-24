package messageSystem;


import app.AccountMap.AccountMap;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author e.shubin
 */
public final class AddressService {

    private List<Address> accountServiceList = new ArrayList<>();

    private AtomicInteger accountServiceCounter = new AtomicInteger();

    public void registerAccountService(AccountMap accountService) {
        accountServiceList.add(accountService.getAddress());
    }

    public synchronized Address getAccountServiceAddress() {
        int index = accountServiceCounter.getAndIncrement();
        if (index >= accountServiceList.size()) {
            index = 0;
        }
        return accountServiceList.get(index);
    }
}
