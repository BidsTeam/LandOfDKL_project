package app.Controller;

import DataBase.Controller.User;
import app.logic.FightFinder;
import app.templater.PageGenerator;
import org.json.JSONObject;

import javax.servlet.ServletException;
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

        response.getWriter().println(PageGenerator.getPage("authform(script).html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void auth(HttpServletRequest request,
                          HttpServletResponse response) {


        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("error", "");
        response.setContentType("text/html;charset=utf-8");
        try {
            int id = 0;
            try {
                id = (int)request.getSession().getAttribute("id");
            } catch (Exception e){
                id = 0;
            }
            System.out.println(id);
            if (id == 0) {
                if (request.getMethod().equalsIgnoreCase("GET")) {
                    response.getWriter().println(PageGenerator.getPage("authform.html", pageVariables));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("login", request.getParameter("login"));
                    jsonObject.put("password", request.getParameter("password"));
                    int userID = user.get(jsonObject);
                    pageVariables.put("id", userID);
                    if (userID != 0) {
                        pageVariables.put("id", userID);
                        request.getSession().setAttribute("id", pageVariables.get("id"));
                        response.getWriter().println(PageGenerator.getPage("login.html", pageVariables));
                    } else {

                        pageVariables.put("error", "Wrong login or password");
                        response.getWriter().println(PageGenerator.getPage("authform.html", pageVariables));
                    }
                }
            } else {
                pageVariables.put("id", id);
                response.getWriter().println(PageGenerator.getPage("login.html", pageVariables));
            }
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

    public void signup(HttpServletRequest request,
                       HttpServletResponse response) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("loginError", "");
        pageVariables.put("passwordError", "");
        pageVariables.put("emailError", "");
        response.setContentType("text/html;charset=utf-8");
        try {
            if (request.getMethod().equalsIgnoreCase("GET")) {
                response.getWriter().println(PageGenerator.getPage("signUpForm.html", pageVariables));
            }
            else {
                JSONObject json = new JSONObject();
                json.put("username", request.getParameter("username"));
                json.put("password", request.getParameter("password"));
                if (user.get(json) == 0) {
                    if (user.add(json)) {
                        response.sendRedirect("/Login/auth");
                    }
                    else {
                        System.out.println("wut?");
                        response.sendRedirect("/Login/signup");
                    }
                } else {
                    pageVariables.put("loginError", "Username taken");
                    response.getWriter().println(PageGenerator.getPage("signUpForm.html", pageVariables));
                }
            }
        } catch(Exception e) {
            System.out.println(e.getMessage() + "in Registration");
        }
    }

}
