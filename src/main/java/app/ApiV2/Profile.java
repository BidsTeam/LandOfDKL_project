package app.ApiV2;

import DAO.logic.UserLogic;
import app.util.AccountCache;
import com.google.gson.Gson;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Profile {
    private UserLogic user = new UserLogic();
    private AccountCache cacheAcc = new AccountCache();

    public JSONObject main(JSONObject json) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", 401);
        return jsonResponse;
    }

    public void show(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userID;
        try {
            userID = (int) request.getSession().getAttribute("id");
        } catch (NullPointerException e) {
            userID = 0;
        }
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        response.setContentType("text/html;charset=utf-8");
        if (userID != 0 ) {
            if (request.getMethod().equalsIgnoreCase("GET")) {
                //JSONObject json;
                try {
                    //User user = Factory.getInstance().getUserDAO().getUserById(userID);
                    System.out.println("test1");
                    UserLogic user = cacheAcc.getUser(userID);
                    //System.out.println("test2" + user.getUsername());

                    body.put("username", user.getUsername());
                    body.put("email", user.getEmail());
                    result.put("status", 200);
                    response.setStatus(HttpServletResponse.SC_OK);
                    //pageVariables.put("username", user.getUsername());
                    //pageVariables.put("email", " test");
                    //response.getWriter().println(PageGenerator.getPage("profile.html", pageVariables));
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
        Gson gson = new Gson();
        String json = gson.toJson(result);
        response.getWriter().println(json);
    }
}
