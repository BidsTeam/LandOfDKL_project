package app.main;

import app.GameMechanics.GameFactory;
import app.WebSocket.CustomWebSocketService;
import app.WebSocket.WebSocketInterfaces.WebSocketService;
import app.servlets.AdminServlet;
import app.servlets.Router;
import app.servlets.SocketServlet;
import app.AccountMap.AccountMap;
import app.AccountMap.AccountMapController;
import app.AccountMap.AccountMapControllerMBean;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hibernate.SessionFactory;
import service.DBService;
import service.DataBase.DataBaseImpl.DBCardServiceImpl;
import service.DataBase.DataBaseImpl.DBUserServiceImpl;
import service.serviceImpl.DBServiceImpl;
import util.HibernateUtil;
import util.LogFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Main {
    public static void main(String[] args) throws Exception {

        String portString = "8080";
        if (args.length >= 1) {
            portString = args[0];
        }

        int port = Integer.valueOf(portString);

        LogFactory.getInstance().getLogger(Main.class).info("Starting at port: " + portString);
        AccountMapControllerMBean serverStatistics = new AccountMapController(AccountMap.getInstance());
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ServerManager:type=AccountServerController");
        mbs.registerMBean(serverStatistics, name);

        HibernateUtil hibernateUtil = new HibernateUtil();
        SessionFactory sessionFactory = hibernateUtil.getSessionFactory();
        DBService dbService = new DBServiceImpl(sessionFactory);
        WebSocketService webSocketService = new CustomWebSocketService(dbService);

        GameFactory.initialize(dbService);

        Server server = new Server(port);

        Router router = new Router(dbService);
        AdminServlet adminServlet = new AdminServlet(dbService);

        SocketServlet socketServlet = new SocketServlet(webSocketService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);


        context.addServlet(new ServletHolder(adminServlet), "/admin/*");
        context.addServlet(new ServletHolder(router), "/api/*");
        context.addServlet(new ServletHolder(socketServlet), "/socket/*");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {resourceHandler, context});

        server.setHandler(handlers);

        server.start();
        server.join();

    }
}
