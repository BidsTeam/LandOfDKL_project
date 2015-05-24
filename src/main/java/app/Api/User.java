package app.Api;

import DAO.logic.UserLogic;
import app.templater.PageGenerator;
import org.hibernate.Session;
import service.DBService;
import util.LogFactory;
import util.ServiceWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


public class User {
    public void top(HttpServletRequest request,
                       HttpServletResponse response, ServiceWrapper serviceWrapper) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<Integer, Object> body = new HashMap<>();
        DBService dbService = serviceWrapper.getDbService();
        try {
            int count;
            try{
                count = Integer.parseUnsignedInt(request.getParameter("count"));
            } catch (Exception e){
                count = 10;
            }
            Session session = dbService.getSession();
            List<UserLogic> userList = null;
            try {
                userList = dbService.getUserService(session).getAllUserRating(count);
            } finally {
                dbService.closeSession(session);
            }


            for (UserLogic user : userList) {
                body.put(user.getId(), user.putAllUserInformation());
            }
            response.setStatus(HttpServletResponse.SC_OK);
            result.put("status", 200);
            result.put("response", body);
            response.getWriter().println(PageGenerator.getJson(result));
        } catch (Exception e){
            LogFactory.getInstance().getLogger(this.getClass()).error("User/top",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
