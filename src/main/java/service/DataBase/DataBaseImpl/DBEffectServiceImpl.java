package service.DataBase.DataBaseImpl;


import DAO.CardDAO;
import DAO.EffectDAO;
import DAO.Impl.CardDAOImpl;
import DAO.Impl.EffectDAOImpl;
import DAO.logic.EffectLogic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import service.DataBase.DBEffectService;

import java.util.List;

public class DBEffectServiceImpl implements DBEffectService {

    EffectDAO effectDAO;

    public DBEffectServiceImpl(Session session) { effectDAO = new EffectDAOImpl(session); }

    @Override
    public EffectLogic getEffect(int id) {
        return effectDAO.getEffect(id);
    }

    @Override
    public EffectLogic getEffectByName(String name) {
        return effectDAO.getEffectByName(name);
    }

    @Override
    public List<EffectLogic> getAllEffects() {
        return effectDAO.getAllEffects();
    }
}
