package service;


import org.hibernate.Session;
import service.DataBase.DBCardService;
import service.DataBase.DBEffectService;
import service.DataBase.DBUserService;

public interface DBService {
    public DBUserService getUserService(Session session);
    public DBCardService getCardService(Session session);
    public DBEffectService getEffectService(Session session);
    public Session getSession();

}
