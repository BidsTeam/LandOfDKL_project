package app.servlets;

import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import app.templater.PageGenerator;
import com.google.gson.Gson;
import org.json.JSONObject;
import service.DBService;
import util.LogFactory;
import util.RouteHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet {

    private AccountMap accountMap = AccountMap.getInstance();
    private DBService dbService;

    public AdminServlet(DBService dbService){
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws  ServletException, IOException {
        try {
            route(request, response);
        } catch (Exception e){
            LogFactory.getInstance().getLogger(this.getClass()).error("Router/doPost",e);
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        try {
            route(request, response);
        } catch (Exception e){
            LogFactory.getInstance().getLogger(this.getClass()).error("Router/doPost",e);
        }
    }

    private void route(HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> body = new HashMap<>();

        result.put("body", body);
        boolean flag = false;
        try {
            int id = (request.getSession().getAttribute("id")!=null) ? (int)request.getSession().getAttribute("id") : 0;
            if (id != 0) {
                UserLogic user = accountMap.getUser(id);
                if (!user.isAdmin()) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    flag = true;
                    routeInvoke(RouteHelper.urlParse(request.getRequestURI()),request,response);
                }
            } else {
                body.put("error", "unauthorized");
                result.put("status", 401);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new Exception(e);
        }
        if (!flag) {
            response.getWriter().println(PageGenerator.getJson(result));
        }
    }

    private void routeInvoke(String[] urlParts,HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        try {
            LogFactory.getInstance().getLogger(this.getClass()).debug("Router.routeInvoke: app.Admin." + urlParts[2].substring(0, 1).toUpperCase() + urlParts[2].substring(1)+"/"+urlParts[3].toLowerCase());
            Class<?> cls = Class.forName("app.Admin." + urlParts[2].substring(0, 1).toUpperCase() + urlParts[2].substring(1));
            Object obj = cls.newInstance();
            Class[] paramTypes = new Class[]{HttpServletRequest.class, HttpServletResponse.class, DBService.class};
            Method method = cls.getMethod(urlParts[3].toLowerCase(), paramTypes);
            Object[] args = new Object[]{request, response,dbService};
            method.invoke(obj, args);
        } catch (Exception e){
            throw new Exception(e);
        }
    }


}
