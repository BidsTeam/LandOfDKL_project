package java.DAO;



import DAO.logic.UserLogic;
import org.hibernate.SessionFactory;
import org.junit.Test;
import service.DBService;
import service.DataBase.DataBaseImpl.DBCardServiceImpl;
import service.DataBase.DataBaseImpl.DBUserServiceImpl;
import service.serviceImpl.DBServiceImpl;
import util.HibernateUtil;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

//todo Спросить, правильно-ли мы понимаем идеологию тестов
public class User {

    HibernateUtil hibernateUtil = new HibernateUtil();
    SessionFactory sessionFactory = hibernateUtil.getSessionFactory();
    DBService dbService = new DBServiceImpl(new DBUserServiceImpl(sessionFactory), new DBCardServiceImpl(sessionFactory));

    @Test
    public void testGetUserByIdCorrect() throws Exception {
        UserLogic user = dbService.getUserService().getUserById(1);
        assertEquals(1, user.getId());
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        UserLogic user = dbService.getUserService().getUserByUsername("admin");
        assertEquals("admin", user.getUsername());
    }

    @Test
    public void testGetUserByAuth() throws Exception {
        UserLogic user = dbService.getUserService().getUserByAuth("admin", "admin");
        assertEquals("admin", user.getUsername());
    }

//    @Test
//    public void testGetUserByAuth() throws Exception {
//        UserLogic user = dbService.getUserService().
//    }

//
//    @Test
//    public void testGetUserByIdWrongIdWithHighId(){
//        try{
//            UserDAO userModel = Factory.getInstance().getUserDAO();
//            UserLogic user = userModel.getUserById(9999999);
//            assertTrue(user == null);
//        } catch(Exception e){
//            assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testGetUserByUsernameCorrect() {
//        try {
//            UserDAO userModel = Factory.getInstance().getUserDAO();
//            UserLogic user = userModel.getUserByUsername("admin");
//            assertNotNull(user);
//        } catch (Exception e) {
//            assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testGetUserByUsernameWrongName() {
//        try {
//            UserDAO userModel = Factory.getInstance().getUserDAO();
//            UserLogic user = userModel.getUserByUsername("admin555tdfsdfaweajkalvbjmdsmflsk");
//            assertTrue(user == null);
//        } catch (Exception e) {
//            assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testGetUserByUsernameWrongEmptyName() {
//        try {
//            UserDAO userModel = Factory.getInstance().getUserDAO();
//            UserLogic user = userModel.getUserByUsername("");
//            assertTrue(user == null);
//        } catch (Exception e) {
//            assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testGetAllUsersTrue(){
//        try {
//            UserDAO userModel = Factory.getInstance().getUserDAO();
//            List<UserLogic> userList = userModel.getAllUsers();
//            assertFalse(userList.isEmpty());
//        } catch (Exception e) {
//            assertTrue(false);
//        }
//    }
}
