package service;


import service.DataBase.DBCardService;
import service.DataBase.DBEffectService;
import service.DataBase.DBUserService;

public interface DBService {
    public DBUserService getUserService();
    public DBCardService getCardService();
    public DBEffectService getEffectService();
}
