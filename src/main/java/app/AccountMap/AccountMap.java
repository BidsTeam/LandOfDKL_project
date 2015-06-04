package app.AccountMap;

import DAO.logic.UserLogic;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
//import org.eclipse.jetty.websocket.api.Session;
import org.hibernate.Session;
import org.json.JSONObject;
import service.DBService;
import settings.ThreadSettings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class AccountMap implements Abonent, Runnable {

    private final Address address = new Address();

    private static volatile ConcurrentHashMap<Integer, UserLogic> cachedAccounts = new ConcurrentHashMap<>();

    private final MessageSystem messageSystem;

    private DBService dbservice;
    public AccountMap(DBService dbService,MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerAccountService(this);
        this.dbservice = dbService;
    }

    public AccountMap(){
        this.messageSystem = null;
        this.dbservice = null;
    }


    public void putUser(int userId) {
        Session session = dbservice.getSession();
        UserLogic user = dbservice.getUserService(session).getUserById(userId);
        cachedAccounts.put(userId, user);
        System.out.println("putted");
        session.close();
    }


    public UserLogic getUser(int id) {
        System.out.println("getted");
        return cachedAccounts.get(id);
    }

    public int getLoggedCounter() {
        return cachedAccounts.size();
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true){
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(ThreadSettings.SERVICE_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
