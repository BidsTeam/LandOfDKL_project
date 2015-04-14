package service.serviceImpl;


import org.hibernate.SessionFactory;
import service.DBService;
import service.DataBase.DBUserService;
import service.DataBase.DataBaseImpl.DBUserServiceImpl;

public class DBServiceImpl implements DBService {
        private DBUserService userService;

        public DBServiceImpl(SessionFactory sessionFactory){
            userService = new DBUserServiceImpl(sessionFactory);
        }

        public DBUserService getUserService(){
            return userService;
        }

}
