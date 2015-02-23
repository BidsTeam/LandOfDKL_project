package app.Controller;

import DataBase.Controller.User;
import app.logic.FightFinder;
import org.json.simple.JSONObject;
import app.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class Login {

    private String login = "";

    FightFinder fightFinder = new FightFinder();
    User user = new User();

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

        JSONObject jObj = new JSONObject();
        jObj.put("action", "create_new_user");
        jObj.put("username", "Secosnd_Users");
        jObj.put("password", "secondpassword");

        String action = jObj.get("action").toString();
        System.out.println(action);
        if (action == "create_game") {
            //Do later
        }
        else if (action == "attack_in_game") {
            //Do later
        } else if (action == "create_new_user") {
            user.add(jObj);

        } else if (action == "login") {
//            JSONObject jsonResult = dbTalker.loginCheck(jObj);
//            send jsonResult back to front
        }
        String login = request.getParameter("login");

        response.setContentType("text/html;charset=utf-8");

//        if (login == null || login.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        } else {
//            response.setStatus(HttpServletResponse.SC_OK);
//        }

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login == null ? "" : login);
        try {
            response.getWriter().println(PageGenerator.getPage("authform.html", pageVariables));
        } catch (Exception e){
            System.err.println(e.getMessage() + " In Login");
        }
    }

    public void name(HttpServletRequest request,
                     HttpServletResponse response) {

        response.setContentType("text/html;charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login == null ? "" : login);
        try {
            response.getWriter().println("bondar");
        } catch (Exception e){
            System.err.println(e.getMessage() +" In Login");
            e.printStackTrace();
        }
    }

}
