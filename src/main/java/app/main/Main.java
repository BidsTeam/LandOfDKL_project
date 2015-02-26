package app.main;

import app.servlets.Router;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONObject;
/**
 * @author v.chibrikov
 */

public class Main {
    public static void main(String[] args) throws Exception {
        //Frontend frontend = new Frontend();
        Router router = new Router();
        JSONObject json = new JSONObject();
        json.put("action", "create_new_user");
        json.put("username", "Secosnd_Users");
        json.put("password", "secondpassword");
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(router), "/");

        server.setHandler(context);

        server.start();
        server.join();



    }
}
