package app.Api;

import DAO.logic.User;
import app.utlit.AccountCache;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreybondar on 26.02.15.
 */
public class Profile {
    private User user = new User();
    private AccountCache cacheAcc = new AccountCache();

    public void main(HttpServletResponse request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/Profile/show/");
    }

    public void show(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userID;
        try {
            userID = (int) request.getSession().getAttribute("id");
        } catch (NullPointerException e) {
            userID = 0;
        }
        Map<String, Object> result = new HashMap<>();
        response.setContentType("text/html;charset=utf-8");
        if (userID != 0 ) {
            if (request.getMethod().equalsIgnoreCase("GET")) {
                //JSONObject json;
                try {
                    //User user = Factory.getInstance().getUserDAO().getUserById(userID);
                    System.out.println("test1");
                    User user = cacheAcc.getUser(userID);
                    System.out.println("test2" + user.getUsername());
                    result.put("username", user.getUsername());
                    result.put("email", user.getEmail());
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    response.setStatus(HttpServletResponse.SC_OK);
                    //pageVariables.put("username", user.getUsername());
                    //pageVariables.put("email", " test");
                    //response.getWriter().println(PageGenerator.getPage("profile.html", pageVariables));
                } catch (Exception e) {
                    result.put("error", "error in finding your account info");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                //TODO - можно будет менять инфу свою внутри профиля
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        Gson gson = new Gson();
        String json = gson.toJson(result);
        response.getWriter().println(json);
    }
}
