package app.main;

import app.servlets.AdminServlet;
import app.servlets.Router;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
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

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");



        //server.setHandler(context);


        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {resourceHandler, context});

        server.setHandler(handlers);

        server.start();
        server.join();

    }
}
