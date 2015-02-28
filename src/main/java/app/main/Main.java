package app.main;

import app.servlets.AdminServlet;
import app.servlets.Router;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
/**
 * @author v.chibrikov
 */

public class Main {
    public static void main(String[] args) throws Exception {
        //Frontend frontend = new Frontend();
        Router router = new Router();


        Server server = new Server(8080);
        AdminServlet adminServlet = new AdminServlet(server);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(adminServlet), "/admin/");
        context.addServlet(new ServletHolder(router), "/api/*");

        server.setHandler(context);

        server.start();
        server.join();

    }
}
