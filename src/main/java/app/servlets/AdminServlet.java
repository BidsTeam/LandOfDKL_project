package app.servlets;

import DataBase.Controller.User;
import app.templater.PageGenerator;
import org.eclipse.jetty.server.Server;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreybondar on 26.02.15.
 */
public class AdminServlet extends HttpServlet {

    private Server server;
    private User user = new User();

    public AdminServlet(Server serverLink) {
        server = serverLink;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws  ServletException, IOException {
        int id = 0;
        try {
            id = (int)request.getSession().getAttribute("id");
        } catch (Exception e){
            id = 0;
        }
        if (id != 0) {
            Map<String, Object> pageVariables = new HashMap<>();
            JSONObject json;
            json = user.getByID(id);
            if ((boolean) json.get("isAdmin") == false) {
                response.sendRedirect("/profile/show");
            } else {
                pageVariables.put("regCounter", user.getRegisterCounter());
                response.getWriter().println(PageGenerator.getPage("adminPage.html", pageVariables));
            }
        } else {
            response.sendRedirect("/login/auth");
        }
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
            //Map<String, Object> pageVariables = new HashMap<>();
            JSONObject json;
            json = user.getByID(id);
            if (!(boolean)json.get("isAdmin")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
                String action = request.getParameter("action");
                if (action.equalsIgnoreCase("stop")) {
                    try {
                        System.out.println("Shutting down");
                        System.exit(0);
                    } catch (Exception e) {
                        System.out.println(e.getMessage() + "Cant stop server, machines are rising");
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }


}
