package app.Api;

import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import app.templater.PageGenerator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import service.DBService;
import util.LogFactory;
import util.MessageList;
import util.UserCardsGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Auth {
    private String login = "";


    public void main(HttpServletRequest request,
                      HttpServletResponse response, DBService dbService) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login == null ? "" : login);


        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);

    }

    public void signup(HttpServletRequest request,
                       HttpServletResponse response, DBService dbService) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> body = new HashMap<>();
        try{

            if (request.getMethod().equalsIgnoreCase("GET")) {
                result.put("error", MessageList.Message.UsePost);
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            } else {
                UserLogic user = new UserLogic(request.getParameter("username"), request.getParameter("password"), request.getParameter("email"));
                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                HashMap<String,String> validateResult = UserLogic.validate(user, validator);
                if (validateResult.isEmpty()){
                    Session session = dbService.getSession();
                    try {
                        if (dbService.getUserService(session).addUser(user)) {
                            request.getSession().setAttribute("id", user.getId());
                            AccountMap.getInstance().putUser(user);
                            body.putAll(user.putAllUserInformation());
                            result.put("status", 200);
                            response.setStatus(HttpServletResponse.SC_OK);
                        } else {
                            result.put("error", MessageList.Message.UserAlreadyExists);
                        }
                    } catch (Exception e) {
                        result.put("status", 500);
                        body.put("error", MessageList.Message.UnknownErrorOnServer);
                        LogFactory.getInstance().getLogger(this.getClass()).error("Auth/signup Error with registration User", e);
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    } finally {
                        dbService.closeSession(session);
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
            LogFactory.getInstance().getLogger(this.getClass()).error("Auth/signup", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void signin(HttpServletRequest request,
                     HttpServletResponse response, DBService dbService) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> body = new HashMap<>();
        Session session = dbService.getSession();
        try {
            int id = (request.getSession().getAttribute("id") != null)?(int)request.getSession().getAttribute("id"):0;
            if (id == 0) {
                if (request.getMethod().equalsIgnoreCase("GET")) {
                    result.put("status", 405);
                    result.put("error", MessageList.Message.UsePost);
                    response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
                } else {
                    String login = request.getParameter("login");
                    String password = request.getParameter("password");
                    UserLogic user = dbService.getUserService(session).getUserByAuth(login, password);
                    if (user == null){
                        result.put("status", 404);
                        body.put("error", MessageList.Message.WrongAuth);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        result.put("status", 200);
                        body.putAll(user.putAllUserInformation());
                        //checkDeck(user.getId(), dbService);
                        request.getSession().setAttribute("id", user.getId());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            } else {
                //todo пользователь уже авторизован, а возврат данных о нем как-то подругому сделаем
                UserLogic user = dbService.getUserService(session).getUserById(id);
                if (user == null){
                    result.put("status", 500);
                    body.put("error", MessageList.Message.WrongSession);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    body.putAll(user.putAllUserInformation());
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
            result.put("response", body);
            response.getWriter().println(PageGenerator.getJson(result));
        } catch (Exception e){
            LogFactory.getInstance().getLogger(this.getClass()).error("Auth/signin", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally{
            dbService.closeSession(session);
        }
    }

    public void drop(HttpServletRequest request,
                       HttpServletResponse response, DBService dbService) {

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
            LogFactory.getInstance().getLogger(this.getClass()).error("Auth/drop", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    public void isauth(HttpServletRequest request, HttpServletResponse response, DBService dbService) {
        JSONObject json = new JSONObject();
        try {
            int id = (request.getSession().getAttribute("id") != null) ? (int) request.getSession().getAttribute("id") : 0;
            json.put("status", 200);
            json.put("isAuth", (id != 0) );
            response.getWriter().println(json.toString());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("Error in isAuth");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    public void checkDeck(int userID, DBService dbService) {
        Session session = dbService.getSession();
        try {
            if (!dbService.getUserService(session).isDeckFull(userID)) {
                UserCardsGenerator cardsGenerator = new UserCardsGenerator(dbService);
                cardsGenerator.generate(userID);
            }
        } finally {
            dbService.closeSession(session);
        }
    }
}
