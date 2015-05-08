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

        public DBServiceImpl(DBUserService userService, DBCardService cardService){
            this.userService = userService;
            this.cardService = cardService;
        }

        public DBUserService getUserService(){
            return userService;
        }

        public DBCardService getCardService() { return cardService; }

}
