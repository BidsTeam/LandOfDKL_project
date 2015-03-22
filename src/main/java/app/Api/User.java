package app.Api;

import DAO.Factory;
import DAO.logic.UserLogic;
import com.google.gson.Gson;
import util.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User {
    public void top(HttpServletRequest request,
                       HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<Integer, Object> body = new HashMap<>();
        try {
            int count;
            try{
                count = Integer.parseUnsignedInt(request.getParameter("count"));
            } catch (Exception e){
                count = 10;
            }
            List<UserLogic> userList = Factory.getInstance().getUserDAO().getAllUserRating(count);
            for (UserLogic user : userList) {
                body.put(user.getId(), UserLogic.putAllUserInformation(user));
            }
            response.setStatus(HttpServletResponse.SC_OK);
            result.put("status", 200);
            result.put("response", body);
            Gson gson = new Gson();
            String json = gson.toJson(result);
            response.getWriter().println(json);
        } catch (Exception e){
            LogFactory.getInstance().getApiLogger().error("User/top",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
