package util;

import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;


    public HibernateUtil(){
        try {
            Configuration configuration = new Configuration();
            configuration.addResource("hibernate.cfg.xml");
            configuration.configure();
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException he) {
            LogFactory.getInstance().getLogger(this.getClass()).fatal("Util.HibernateUtil/static Error creating Session:",he);
            throw new ExceptionInInitializerError(he);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
