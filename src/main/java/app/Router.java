package app;

import DataBase.Controller.User;
import DataBase.DB;
import app.Controller.Login;
import app.logic.FightFinder;
import app.templater.PageGenerator;
import org.json.simple.JSONObject;

import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Router extends HttpServlet {

    private String login = "";

    FightFinder fightFinder = new FightFinder();
    User user = new User(DB.getStatement());

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
//        Pattern p = Pattern.compile("(:\\/\\/.*?)\\/(.*)");
//        Matcher m = p.matcher(request.getRequestURL());
//        String url = "";
//        while(m.find()){
//            url = m.group();
//        }

        HttpSession session = request.getSession();
        if (session.getAttribute("userID") == null) {
            Login newLogin = new Login();
            newLogin.auth(request, response);
        }
        else {
            String url = request.getServletPath();
            try {

                System.out.println(url);
                String[] urlParts = url.split("/");
                Class<?> cls = Class.forName("app.Controller." + urlParts[1].substring(0, 1).toUpperCase() + urlParts[1].substring(1));
                Object obj = cls.newInstance();
                Class[] paramTypes = new Class[]{HttpServletRequest.class, HttpServletResponse.class};
                Method method = cls.getMethod(urlParts[2], paramTypes);
                Object[] args = new Object[]{request, response};
                method.invoke(obj, args);
            } catch (Exception e) {
                System.out.println(e.getMessage() + " In Router");
            }
        }
        return;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

//        String login = request.getParameter("login");
//
//        response.setContentType("text/html;charset=utf-8");
//
//        if (login == null || login.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        } else {
//            response.setStatus(HttpServletResponse.SC_OK);
//        }
//
//        Map<String, Object> pageVariables = new HashMap<>();
//        pageVariables.put("lastLogin", login == null ? "" : login);
//
//        response.getWriter().println(PageGenerator.getPage("authform.html", pageVariables));
    }
}