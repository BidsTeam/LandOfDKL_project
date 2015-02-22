package app.Controller;

import DataBase.Controller.User;
import DataBase.DB;
import app.logic.FightFinder;
import org.json.simple.JSONObject;
import app.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class Login extends HttpServlet {

    private String login = "";

    FightFinder fightFinder = new FightFinder();
    User user = new User(DB.getStatement());

    public void main(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login == null ? "" : login);

        response.getWriter().println(PageGenerator.getPage("authform.html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void auth(HttpServletRequest request,
                          HttpServletResponse response) {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        JSONObject json = new JSONObject();
        json.put("username", login);
        json.put("password", password);

        //String action = jObj.get("action").toString();
        //System.out.println(action);

        User userDB = new User(DB.getStatement());

        if (userDB.checkLogin(json)) {
            response.setStatus(HttpServletResponse.SC_OK);
            request.getSession().setAttribute("userID", json.get("username").toString());
        }

        response.setContentType("text/html;charset=utf-8");

        if (login == null || login.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login == null ? "" : login);
        try {
            response.getWriter().println(PageGenerator.getPage("authform.html", pageVariables));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
