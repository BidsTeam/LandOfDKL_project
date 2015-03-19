package app.Api;

import DAO.Factory;
import DAO.logic.UserLogic;
import app.logic.FightFinder;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Auth {
    private String login = "";

    FightFinder fightFinder = new FightFinder();

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
                UserLogic user = new UserLogic();
                user.setUsername((request.getParameter("username")));
                user.setPassword(request.getParameter("password"));
                user.setEmail((request.getParameter("email")));
                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                HashMap<String,String> validateResult = UserLogic.validate(user, validator);
                if (validateResult.isEmpty()){
                    try {
                        Factory.getInstance().getUserDAO().addUser(user);
                        request.getSession().setAttribute("id", user.getId());
                        body.putAll(UserLogic.putAllUserInformation(user));
                        result.put("status", 200);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } catch (Exception e) {
                        result.put("status", 500);
                        body.put("error", "Undefined error in server");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } else {
                    result.put("status", 400);
                    body.put("error",validateResult);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
            result.put("response", body);
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
                    String login = request.getParameter("login");
                    String password = request.getParameter("password");
                    UserLogic user = Factory.getInstance().getUserDAO().getUserByAuth(login, password);
                    if (user == null){
                        result.put("status", 404);
                        body.put("error","Wrong password");
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        result.put("status", 200);
                        body.putAll(UserLogic.putAllUserInformation(user));
                        request.getSession().setAttribute("id", user.getId());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            } else {
                //todo пользователь уже авторизован, а возврат данных о нем как-то подругому сделаем
                UserLogic user = Factory.getInstance().getUserDAO().getUserById(id);
                if (user == null){
                    result.put("status", 301);
                    body.put("error","Wrong session");
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    body.putAll(UserLogic.putAllUserInformation(user));
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
            result.put("response", body);
            Gson gson = new Gson();
            String json = gson.toJson(result);
            response.getWriter().println(json);
        } catch (Exception e){
            System.err.println(e.getMessage() + " In Login");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void drop(HttpServletRequest request,
                       HttpServletResponse response) {

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        result.put("status", 200);
        body.put("result","ok");
        result.put("response", body);
        request.getSession().setAttribute("id", 0);
        response.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new Gson();
        String json = gson.toJson(result);
        try {
            response.getWriter().println(json);
        } catch (Exception e){
            System.err.println(e.getMessage() + " In ");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
