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
    private SessionFactory sessionFactory;

    public EffectDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public EffectLogic getEffect(int id) {
        Session session = null;
        EffectLogic effect = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            effect = (EffectLogic) session.get(EffectLogic.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getEffect");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return effect;
    }

    @Override
    public EffectLogic getEffectByName(String name) {
        Session session = null;
        EffectLogic effect = null;
        try {
            session = sessionFactory.openSession();
            effect = (EffectLogic) session.createCriteria( UserLogic.class ).
                    add( Restrictions.eq("name", name) ).
                    uniqueResult();
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("SQL error in getEffectByName");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return effect;
    }

    @Override
    public List<EffectLogic> getAllEffects() {
        Session session = null;
        List<EffectLogic> effectList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            effectList = session.createCriteria(EffectLogic.class).list();
//            effectList = session.createQuery("from EffectLogic").list();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
            return effectList;
        }
    }
}
