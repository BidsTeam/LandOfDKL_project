package util;

import messageSystem.MessageSystem;
import service.DBService;

public class ServiceWrapper {
    private DBService dbService;

    private MessageSystem messageSystem;

    public ServiceWrapper(DBService dbService, MessageSystem messageSystem){
        this.dbService = dbService;
        this.messageSystem = messageSystem;
    }

    public DBService getDbService() {
        return dbService;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
}
