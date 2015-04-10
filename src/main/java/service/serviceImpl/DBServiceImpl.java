package service.serviceImpl;


import service.DBService;
import service.DataBase.DBUserService;

public class DBServiceImpl implements DBService {
        private DBUserService userService;

        public DBServiceImpl(DBUserService userService){
            this.userService = userService;
        }

        public DBUserService getUserService(){
            return userService;
        }

}
