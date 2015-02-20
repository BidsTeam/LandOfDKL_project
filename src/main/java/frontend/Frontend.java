package frontend;

import logic.DBMediator;
import logic.FightFinder;
import logic.FightInstance;
import org.json.simple.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class Frontend extends HttpServlet {

    private String login = "";

    FightFinder fightFinder = new FightFinder();
    DBMediator dbTalker = new DBMediator();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login == null ? "" : login);

        response.getWriter().println(PageGenerator.getPage("authform.html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");

        response.setContentType("text/html;charset=utf-8");

        if (login == null || login.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login == null ? "" : login);

        response.getWriter().println(PageGenerator.getPage("authform.html", pageVariables));
    }



    public void parseJson(JSONObject jObj) {
        String action = jObj.get("action").toString();
        System.out.println(action);
        if (action == "create_game") {
            //Do later
        }
        else if (action == "attack_in_game") {
            //Do later
        } else if (action == "create_new_user") {
            dbTalker.newUserTableSQL(jObj);
        } else if (action == "login") {
            JSONObject jsonResult = dbTalker.loginCheck(jObj);
            //send jsonResult back to front
        }
    }

}
