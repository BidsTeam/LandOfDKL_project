package app.Api;

import StubClasses.DBServiceStub;
import TestSetups.TestsCore;
import app.AccountMap.AccountMap;
import messageSystem.MessageSystem;
import org.json.JSONObject;
import org.junit.*;
import org.junit.Test;
import service.DBService;
import util.LogFactory;
import util.ServiceWrapper;

import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class AuthTest extends TestsCore {

    static DBService  dbService = new DBServiceStub();
    static final MessageSystem messageSystem = new MessageSystem();
    ServiceWrapper serviceWrapper = new ServiceWrapper(dbService,messageSystem);

    @BeforeClass
    public static void before(){
        final Thread accountServiceThread = new Thread(new AccountMap(dbService,messageSystem));
        accountServiceThread.setDaemon(true);
        accountServiceThread.setName("Account Map");
    }


    private HttpServletRequest getMockedRequest() {
        HttpServletRequest request =  mock(HttpServletRequest.class);
        return request;
    }

    private HttpServletResponse getMockedResponse(StringWriter stringWriter) {
        HttpServletResponse response = mock(HttpServletResponse.class);

        final PrintWriter writer = new PrintWriter(stringWriter);
        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("problem in getWriter from mock");
        }
        return response;
    }


    @Test
    public void testSignin() throws Exception {
        HttpServletRequest request = getMockedRequest();
        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("id")).thenReturn(0);
        when(request.getParameter("login")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("admin");
        when(request.getMethod()).thenReturn("POST");


        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);
        String correctResponse = "{\"response\":{\"is_admin\":false,\"level\":1,\"registration\":1429031051000,\"id\":0,\"email\":\"admin@mail.ru\",\"username\":\"admin\"},\"status\":200}\n";
        Auth auth = new Auth();
        auth.signin(request, response, serviceWrapper);
        JSONObject correctJSON = new JSONObject(correctResponse);
        JSONObject actualJSON = new JSONObject(stringWriter.toString());
        correctJSON.getJSONObject("response").remove("registration");
        actualJSON.getJSONObject("response").remove("registration");
        assertEquals(correctJSON.toString(), actualJSON.toString());
    }

    @Test
    public void testSignup() throws Exception {
        HttpServletRequest request = getMockedRequest();
        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("id")).thenReturn(0);
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("testPassword");
        when(request.getParameter("email")).thenReturn("test@mail.ru");
        when(request.getMethod()).thenReturn("POST");
        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);
        String correctResponse = "{\"response\":{\"is_admin\":false,\"level\":1,\"registration\":1432208706074,\"id\":0,\"email\":\"test@mail.ru\",\"username\":\"testUser\"},\"status\":200}\n";
        Auth auth = new Auth();
        auth.signup(request, response, serviceWrapper);
        JSONObject correctJSON = new JSONObject(correctResponse);
        JSONObject actualJSON = new JSONObject(stringWriter.toString());
        correctJSON.getJSONObject("response").remove("registration");
        actualJSON.getJSONObject("response").remove("registration");
        assertEquals(correctJSON.toString(), actualJSON.toString());
    }

    @Test
    public void testDrop() throws Exception {
        HttpServletRequest request = getMockedRequest();
        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);

        String correctResponse = "{\"response\":{\"result\":\"ok\"},\"status\":200}\n";

        Auth auth = new Auth();
        auth.drop(request, response, serviceWrapper);
        JSONObject correctJSON = new JSONObject(correctResponse);
        JSONObject actualJSON = new JSONObject(stringWriter.toString());
        correctJSON.getJSONObject("response");
        actualJSON.getJSONObject("response");
        assertEquals(correctJSON.toString(), actualJSON.toString());

    }

//    @Test
//    public void testAlreadyExists() throws Exception {
//        HttpServletRequest request = getMockedRequest();
//        HttpSession httpSession = mock(HttpSession.class);
//        when(request.getSession()).thenReturn(httpSession);
//        when(httpSession.getAttribute("id")).thenReturn(0);
//        when(request.getParameter("username")).thenReturn("admin");
//        when(request.getParameter("password")).thenReturn("wrongAdminPassword");
//        when(request.getParameter("email")).thenRe                                                            turn("admin@mail.ru");
//        when(request.getMethod()).thenReturn("POST");
//        DBService dbServiceMock = new DBServiceStub();
//
//        final StringWriter stringWriter = new StringWriter();
//        HttpServletResponse response = getMockedResponse(stringWriter);
//    }

//    @Test
//    public void testSignin() throws Exception {
//
//    }
//
//    @Test
//    public void testDrop() throws Exception {
//
//    }
//
//    @Test
//    public void testIsauth() throws Exception {
//
//    }
}