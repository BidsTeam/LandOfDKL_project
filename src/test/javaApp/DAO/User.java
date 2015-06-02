package javaApp.DAO;


/*
DISCLAIMER - тесты классов DAO интеграционные, то есть зависят и от базы данных
Пытался сделать их максимально абстрагированными от содержимиого бд, сложно сказать
насколько мне это удалось
 */
import DAO.logic.UserLogic;
import TestSetups.TestsCore;
import app.AccountMap.AccountMap;
import jdk.nashorn.internal.runtime.ECMAException;
import messageSystem.MessageSystem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import service.DBService;
import service.serviceImpl.DBServiceImpl;
import util.ServiceWrapper;

import java.lang.Exception;
import java.util.List;

import static org.junit.Assert.*;

public class User extends TestsCore {

    private final DBService dbService = new DBServiceImpl(sessionFactory);
    private final MessageSystem messageSystem = new MessageSystem();


    @Before
    public void before(){
      //  ServiceWrapper serviceWrapper = new ServiceWrapper(dbService,messageSystem);
        final Thread accountServiceThread = new Thread(new AccountMap(dbService,messageSystem));
        accountServiceThread.setDaemon(true);
        accountServiceThread.setName("Account Map");
    }

    @Test
    public void testGetUserByIdCorrect() throws Exception {
        UserLogic user = dbService.getUserService(dbService.getSession()).getUserById(1);
        assertEquals(1, user.getId());
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        Session session = dbService.getSession();
        UserLogic user = dbService.getUserService(session).getUserByUsername("admin");
        session.close();
        assertEquals("admin", user.getUsername());
    }

    @Test
    public void testGetUserByAuth() throws Exception {
        Session session = dbService.getSession();
        UserLogic user = dbService.getUserService(session).getUserByAuth("admin", "admin",messageSystem);
        session.close();
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
        session.close();
        assertEquals(user.getId(), 1);
    }

    @Test
    public void testGetCounter() throws Exception {
        Session session = dbService.getSession();
        int result = dbService.getUserService(session).getUserCounter();
        session.close();
        assertNotNull(result); //Тест интеграционный, смысла проверять на конкретное число нет
    }

    @Test
    public void testGetUserRate() throws Exception {
        Session session = dbService.getSession();
        List<UserLogic> userRatings = dbService.getUserService(session).getAllUserRating(1);
        session.close();
        assertNotNull(userRatings);
    }

}
