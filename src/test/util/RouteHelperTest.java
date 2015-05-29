package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class RouteHelperTest {

    @Test
    public void testUrlParse() throws Exception {
        String url = "/directive/class/method";
        String[] str = RouteHelper.urlParse(url);
        assertEquals(str[0],"directive");
        assertEquals(str[1],"class");
        assertEquals(str[2],"method");
    }
}