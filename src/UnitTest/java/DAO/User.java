package DAO;



import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;


public class User {

    @Test
    public void testEmptyCollection() {
        Collection collection = new ArrayList();
        assertNotNull(collection);
        assertTrue(collection.isEmpty());
    }
}
