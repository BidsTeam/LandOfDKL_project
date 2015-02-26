package app.Controller;

import DataBase.Controller.User;
import app.templater.PageGenerator;
import org.json.JSONObject;

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
        response.setContentType("text/html;charset=utf-8");
        if (userID != 0 ) {
            Map<String, Object> pageVariables = new HashMap<>();
            if (request.getMethod().equalsIgnoreCase("GET")) {
                JSONObject json;
                json = user.getByID(userID);
                System.out.println(json.get("username").toString());
                pageVariables.put("username", json.get("username").toString());
                pageVariables.put("email", json.get("email").toString() + " - test");
                response.getWriter().println(PageGenerator.getPage("profile.html", pageVariables));
            } else {
                //TODO - можно будет менять инфу свою внутри профиля
            }
        } else {
            response.sendRedirect("/Login/auth");
        }
    }
}
