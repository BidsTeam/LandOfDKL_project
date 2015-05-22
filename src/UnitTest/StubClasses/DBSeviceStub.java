package StubClasses;

import org.hibernate.Session;
import org.mockito.Mockito;
import service.DBService;
import service.DataBase.DBCardService;
import service.DataBase.DBEffectService;
import service.DataBase.DBUserService;

/**
 * Created by andreybondar on 21.05.15.
 */
public class DBSeviceStub implements DBService {
    @Override
    public DBUserService getUserService(Session session) {
        DBUserService dbUserService = new DBUserServiceStub();
        return dbUserService;
    }

    @Override
    public DBCardService getCardService(Session session) {
        return null;
    }

    @Override
    public DBEffectService getEffectService(Session session) {
        return null;
    }

    @Override
    public Session getSession() {
        Session session = Mockito.mock(Session.class);
        return session;
    }

    @Override
    public void closeSession(Session session) {

    }
}
