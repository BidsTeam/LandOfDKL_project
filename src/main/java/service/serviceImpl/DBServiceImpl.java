package service.serviceImpl;


import org.hibernate.SessionFactory;
import service.DBService;
import service.DataBase.DBCardService;
import service.DataBase.DBUserService;
import service.DataBase.DataBaseImpl.DBCardServiceImpl;
import service.DataBase.DataBaseImpl.DBUserServiceImpl;

public class DBServiceImpl implements DBService {
        private DBUserService userService;
        private DBCardService cardService;

        public DBServiceImpl(SessionFactory sessionFactory){
            userService = new DBUserServiceImpl(sessionFactory);
            cardService = new DBCardServiceImpl(sessionFactory);

        }

        public DBUserService getUserService(){
            return userService;
        }

        public DBCardService getCardService() { return cardService; }

}
