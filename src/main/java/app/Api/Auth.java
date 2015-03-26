package app.Api;

import DAO.Factory;
import DAO.logic.UserLogic;
import app.templater.PageGenerator;
import util.LogFactory;

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


    public void main(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login == null ? "" : login);


        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);

    }

    public void signup(HttpServletRequest request,
                       HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> body = new HashMap<>();
        try{

            if (request.getMethod().equalsIgnoreCase("GET")) {
                result.put("error","Please use POST method");
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            } else {
                UserLogic user = new UserLogic(request.getParameter("username"), request.getParameter("password"), request.getParameter("email"));
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
                        LogFactory.getInstance().getApiLogger().error("Auth/signup Error with registration User", e);
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } else {
                    result.put("status", 400);
                    body.put("error",validateResult);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
            result.put("response", body);
            response.getWriter().println(PageGenerator.getJson(result));
        } catch (Exception e){
            LogFactory.getInstance().getApiLogger().error("Auth/signup", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void signin(HttpServletRequest request,
                     HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> body = new HashMap<>();
        try {
            int id = (request.getSession().getAttribute("id") != null)?(int)request.getSession().getAttribute("id"):0;
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

            response.getWriter().println(PageGenerator.getJson(result));
        } catch (Exception e){
            LogFactory.getInstance().getApiLogger().error("Auth/signin", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void drop(HttpServletRequest request,
                       HttpServletResponse response) {

        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> body = new HashMap<>();
        result.put("status", 200);
        body.put("result","ok");
        result.put("response", body);
        request.getSession().setAttribute("id", 0);
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().println(PageGenerator.getJson(result));
        } catch (Exception e){
            LogFactory.getInstance().getApiLogger().error("Auth/drop", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
