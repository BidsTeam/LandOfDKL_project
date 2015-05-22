package TestSetups;

import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;
import util.HibernateUtil;

/**
 * Created by andreybondar on 20.05.15.
 */
@Ignore
public class TestsCore {
    static private HibernateUtil hibernateUtil;
    static protected SessionFactory sessionFactory;
    @BeforeClass
    public static void connectToDB() {
        hibernateUtil = new HibernateUtil();
        sessionFactory = hibernateUtil.getSessionFactory();
    }
}
