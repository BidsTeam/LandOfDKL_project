package app.Api;

import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import app.templater.PageGenerator;
import com.google.gson.Gson;
import service.DBService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Profile {
    private UserLogic user = new UserLogic();
    private AccountMap cacheAcc = AccountMap.getInstance();

    public void main(HttpServletResponse request, HttpServletResponse response, DBService dbService) throws IOException {
        response.sendRedirect("/Profile/show/");
    }

    public void show(HttpServletRequest request, HttpServletResponse response, DBService dbService) throws IOException {
        int userID;
        try {
            userID = (int) request.getSession().getAttribute("id");
        } catch (NullPointerException e) {
            userID = 0;
        }
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> body = new HashMap<>();
        response.setContentType("text/html;charset=utf-8");
        if (userID != 0 ) {
            if (request.getMethod().equalsIgnoreCase("GET")) {
                try {
                    System.out.println("test1");
                    UserLogic user = cacheAcc.getUser(userID);
                    body.put("username", user.getUsername());
                    body.put("email", user.getEmail());
                    result.put("status", 200);
                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (Exception e) {
                    body.put("error", "error in finding your account info");
                    result.put("status", 500);
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                //TODO - можно будет менять инфу свою внутри профиля
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            }
        } else {
            body.put("error", "unauthorizated");
            result.put("status", 401);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        result.put("body", body);

        response.getWriter().println(PageGenerator.getJson(result));
    }
}
