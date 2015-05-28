package DAO.Impl;


import DAO.EffectDAO;
import DAO.logic.CardLogic;
import DAO.logic.EffectLogic;
import DAO.logic.UserLogic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import util.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class EffectDAOImpl implements EffectDAO {
    private Session session;

    public EffectDAOImpl(Session session) {
        this.session = session;
    }

    @Override
    public EffectLogic getEffect(int id) {
        EffectLogic effect = null;
        effect = (EffectLogic) session.get(EffectLogic.class, id);
        return effect;
    }

    @Override
    public EffectLogic getEffectByName(String name) {
        EffectLogic effect = null;
        effect = (EffectLogic) session.createCriteria( EffectLogic.class ).
                add( Restrictions.eq("name", name) ).
                uniqueResult();
        return effect;
    }

    @Override
    public List<EffectLogic> getAllEffects() {
        List<EffectLogic> effectList = new ArrayList<>();
        effectList = session.createCriteria(EffectLogic.class).list();
        return effectList;
    }
}
