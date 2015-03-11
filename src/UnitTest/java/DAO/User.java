package DAO;



import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import DAO.logic.UserLogic;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;


public class User {

    @Test
    public void testGetUserById() {
        try {
            UserDAO userModel = Factory.getInstance().getUserDAO();
            UserLogic user = userModel.getUserById(1);
            assertNotNull(user);
            assertTrue(!user.getEmail().isEmpty());
            assertTrue(!user.getUsername().isEmpty());
            assertTrue(user.getId() == 1);
            assertTrue(user.getLevel() > 0);
            assertTrue(!user.getPassword().isEmpty());
            //assertTrue(user.getRegistration());
            user = userModel.getUserById(999999);
            assertTrue(user == null);
            user = userModel.getUserById(-3);
            assertTrue(user == null);

        } catch (Exception e){
            assertTrue(false);
        }
        Collection collection = new ArrayList();
        assertNotNull(collection);
        assertTrue(collection.isEmpty());
    }
}
