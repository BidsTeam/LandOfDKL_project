package app.GameMechanics;

import DAO.logic.UserLogic;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    UserLogic user = new UserLogic("mock", "mock", "mock");

    @Test
    public void testLethalDamage() throws Exception {
        Player player = new Player(user);
        boolean result = player.takeDamage(30);
        assertEquals(result, false);
    }

    @Test
    public void testTakeTenDamage() throws Exception {
        Player player = new Player(user);
        player.takeDamage(10);
        assertEquals(10, player.getHealth());
    }
}