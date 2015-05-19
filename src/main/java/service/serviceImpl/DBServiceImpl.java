package service.serviceImpl;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import service.DBService;
import service.DataBase.DBCardService;
import service.DataBase.DBEffectService;
import service.DataBase.DBUserService;
import service.DataBase.DataBaseImpl.DBCardServiceImpl;
import service.DataBase.DataBaseImpl.DBEffectServiceImpl;
import service.DataBase.DataBaseImpl.DBUserServiceImpl;

public class DBServiceImpl implements DBService {
        private SessionFactory sessionFactory;

        public DBServiceImpl(SessionFactory sessionFactory){
            this.sessionFactory = sessionFactory;
        }

        public DBUserService getUserService(Session session){ return new DBUserServiceImpl(session); }

        public DBCardService getCardService(Session session) { return new DBCardServiceImpl(session); }

        public DBEffectService getEffectService(Session session) { return new DBEffectServiceImpl(session); }

        public Session getSession(){ return sessionFactory.openSession(); }

}
