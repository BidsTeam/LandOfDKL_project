package DAO;



import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import DAO.logic.UserLogic;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class User {

    @Test
    public void testGetUserByIdCorrect() {
        try {
            UserDAO userModel = Factory.getInstance().getUserDAO();
            UserLogic user = userModel.getUserById(1);
            assertNotNull(user);
        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void testGetUserByIdWithNegativeId(){
        try{
            UserDAO userModel = Factory.getInstance().getUserDAO();
            UserLogic user = userModel.getUserById(-3);
            assertTrue(user == null);
        } catch(Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void testGetUserByIdWrongIdWithHighId(){
        try{
            UserDAO userModel = Factory.getInstance().getUserDAO();
            UserLogic user = userModel.getUserById(9999999);
            assertTrue(user == null);
        } catch(Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void testGetUserByUsernameCorrect() {
        try {
            UserDAO userModel = Factory.getInstance().getUserDAO();
            UserLogic user = userModel.getUserByUsername("admin");
            assertNotNull(user);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetUserByUsernameWrongName() {
        try {
            UserDAO userModel = Factory.getInstance().getUserDAO();
            UserLogic user = userModel.getUserByUsername("admin555tdfsdfaweajkalvbjmdsmflsk");
            assertTrue(user == null);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetUserByUsernameWrongEmptyName() {
        try {
            UserDAO userModel = Factory.getInstance().getUserDAO();
            UserLogic user = userModel.getUserByUsername("");
            assertTrue(user == null);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetAllUsersTrue(){
        try {
            UserDAO userModel = Factory.getInstance().getUserDAO();
            List<UserLogic> userList = userModel.getAllUsers();
            assertFalse(userList.isEmpty());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
