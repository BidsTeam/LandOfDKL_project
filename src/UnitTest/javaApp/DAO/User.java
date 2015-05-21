package javaApp.DAO;



import DAO.logic.UserLogic;
import TestSetups.TestsCore;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import service.DBService;
import service.serviceImpl.DBServiceImpl;

import java.lang.Exception;
import java.util.List;

import static org.junit.Assert.*;

//todo Спросить, правильно-ли мы понимаем идеологию тестов
public class User extends TestsCore {

    DBService dbService = new DBServiceImpl(sessionFactory);

    @Test
    public void testGetUserByIdCorrect() throws Exception {
        UserLogic user = dbService.getUserService(dbService.getSession()).getUserById(1);
        assertEquals(1, user.getId());
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        UserLogic user = dbService.getUserService(dbService.getSession()).getUserByUsername("admin");
        assertEquals("admin", user.getUsername());
    }

    @Test
    public void testGetUserByAuth() throws Exception {
        UserLogic user = dbService.getUserService(dbService.getSession()).getUserByAuth("admin", "admin");
        assertEquals("admin", user.getUsername());
    }

    @Test
    public void testAddUser() throws Exception {
        Session session = dbService.getSession();
        Transaction tx = session.beginTransaction();
        UserLogic user = new UserLogic("TestUser", "TestPassword", "Test@mail.ru");
        dbService.getUserService(session).addUser(user);
        UserLogic resultUser = dbService.getUserService(session).getUserByUsername("TestUser");
        tx.rollback();
        session.close();
        assertEquals(resultUser.getUsername(), "TestUser");
    }

    @Test
    public void testDeleteUser() throws Exception {
        Session session = dbService.getSession();
        Transaction tx = session.beginTransaction();
        UserLogic user = dbService.getUserService(session).getUserByUsername("helloworld");
        dbService.getUserService(session).deleteUser(user);
        UserLogic resultUser = dbService.getUserService(session).getUserByUsername("helloworld");
        tx.rollback();
        session.close();
        assertNull(resultUser);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        Session session = dbService.getSession();
        List<UserLogic> userList = dbService.getUserService(session).getAllUsers();
        UserLogic user = userList.get(0);
        assertEquals(user.getId(), 1);
    }

//    @Test
//    public void testGetUserByAuth() throws Exception {
//        UserLogic user = dbService.getUserService().
//    }
//
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
