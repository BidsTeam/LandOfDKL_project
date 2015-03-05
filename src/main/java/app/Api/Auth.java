package app.Api;

import DAO.Factory;
import DAO.logic.User;
import app.logic.FightFinder;
import app.util.AccountCache;
import com.google.gson.Gson;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 *
 * Контр
 *
 */
public class Auth {
    private String login = "";

    FightFinder fightFinder = new FightFinder();
    private User user = new User();
    private AccountCache accountCache = new AccountCache();

    public void main(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login == null ? "" : login);

        //response.getWriter().println(PageGenerator.getPage("authform(script).html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);

    }

    public void signup(HttpServletRequest request,
                       HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        try{
            if (request.getMethod().equalsIgnoreCase("GET")) {
                result.put("error","Please use POST method");
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            } else {
                JSONObject json = new JSONObject();
                User user = new User();
                user.setUsername((request.getParameter("login")));
                user.setPassword(request.getParameter("password"));
                user.setEmail((request.getParameter("email"))); //todo Проверять регекспом на валидный email
                try {
                    Factory.getInstance().getUserDAO().addUser(user);
                    request.getSession().setAttribute("id", user.getId());
                    putAllUserInformation(user, body);
                    result.put("status", 200);
                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (Exception e) { // Должно быть два exception, один наш, другой реально ошибка
                    result.put("status", 500);
                    body.put("error", "Username or email error");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
            result.put("body", body);
            Gson gson = new Gson();
            String json = gson.toJson(result);
            response.getWriter().println(json);
        } catch (Exception e){
            System.err.println(e.getMessage() + " In Login");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    public void signin(HttpServletRequest request,
                     HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        try {
            int id = 0;
            try {
                id = (int)request.getSession().getAttribute("id");
            } catch (Exception e){
                id = 0;
            }
            if (id == 0) {
                if (request.getMethod().equalsIgnoreCase("GET")) {
                    result.put("status", 405);
                    body.put("error","Please use POST method");
                    response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("login", request.getParameter("login"));
                    jsonObject.put("password", request.getParameter("password"));
                    //System.out.println("test");
                    User user = Factory.getInstance().getUserDAO().getUserByAuth(request.getParameter("login"),request.getParameter("password"));
                    if (user == null){
                        result.put("status", 400);
                        body.put("error","Wrong password");
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        result.put("status", 200);
                        putAllUserInformation(user, body);
                        accountCache.putUser(user);
                        request.getSession().setAttribute("id", user.getId());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            } else {
                User user = Factory.getInstance().getUserDAO().getUserById(id);
                if (user == null){
                    result.put("status", 301);
                    body.put("error","Wrong session");
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    putAllUserInformation(user,body);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
            result.put("body", body);
            Gson gson = new Gson();
            String json = gson.toJson(result);
            response.getWriter().println(json);
        } catch (Exception e){
            System.err.println(e.getMessage() + " In Login");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    private void putAllUserInformation(User user,Map<String, Object> result){
        result.put("id",        user.getId());
        result.put("username",  user.getUsername());
        result.put("registration",user.getRegistration().getTime());
        result.put("is_admin",  user.isAdmin());
        result.put("email",     user.getEmail());
        return;
    }
}
