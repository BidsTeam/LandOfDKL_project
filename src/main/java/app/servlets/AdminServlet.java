package app.servlets;

import DAO.Factory;
import DAO.logic.UserLogic;
import app.AccountCache.AccountCache;
import com.google.gson.Gson;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet {

    private AccountCache accountCache = AccountCache.getInstance();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws  ServletException, IOException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        int id = 0;
        try {
            id = (int)request.getSession().getAttribute("id");
        } catch (Exception e){
            id = 0;
        }
        try {
            if (id != 0) {
                UserLogic user = accountCache.getUser(id);
                if (!user.isAdmin()) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    int usersCounter = Factory.getInstance().getUserDAO().getUserCounter();
                    int loginedCounter = accountCache.getLoggedCounter();
                    body.put("logined", loginedCounter);
                    body.put("registrated", usersCounter);
                    result.put("status", 200);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                body.put("error", "unauthorized");
                result.put("status", 401);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        result.put("body", body);
        Gson gson = new Gson();
        String json = gson.toJson(result);
        response.getWriter().println(json);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        int id = 0;
        try {
            id = (int)request.getSession().getAttribute("id");
        } catch (Exception e){
            id = 0;
        }
        if (id != 0) {
            UserLogic user = accountCache.getUser(id);
            if (!user.isAdmin()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
                String action = request.getParameter("action");
                if (action.equalsIgnoreCase("stop")) {
                    try {
                        System.out.println("Shutting down");
                        response.setStatus(HttpServletResponse.SC_OK);
                        System.exit(0);
                    } catch (Exception e) {
                        System.out.println(e.getMessage() + "Cant stop server, machines are rising");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }


}
