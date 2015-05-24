package TestSetups;

import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;
import util.HibernateUtil;


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
