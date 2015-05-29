package DAO.Impl;

import DAO.logic.EffectLogic;
import DAO.logic.UserLogic;
import TestSetups.TestsCore;
import messageSystem.MessageSystem;
import org.junit.Test;
import service.DBService;
import service.serviceImpl.DBServiceImpl;
import util.ServiceWrapper;

import java.util.List;

import static org.junit.Assert.*;

public class EffectDAOImplTest extends TestsCore {

    static DBService dbService = new DBServiceImpl(sessionFactory);
    final static MessageSystem messageSystem = new MessageSystem();
    ServiceWrapper serviceWrapper = new ServiceWrapper(dbService,messageSystem);


    @Test
    public void testGetEffect() throws Exception {
        EffectLogic effect= dbService.getEffectService(dbService.getSession()).getEffect(1);
        assertEquals("boom", effect.getName());
    }

    @Test
    public void testGetEffectByName() throws Exception {
        EffectLogic effect= dbService.getEffectService(dbService.getSession()).getEffectByName("boom");
        assertEquals(1, effect.getId());
    }

    @Test
    public void testGetAllEffects() throws Exception {
        List<EffectLogic> effectList= dbService.getEffectService(dbService.getSession()).getAllEffects();
        assertFalse(effectList.isEmpty());
    }
}