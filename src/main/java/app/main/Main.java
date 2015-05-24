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
import messageSystem.MessageSystem;
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
import util.ServiceWrapper;

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
//        AccountMapControllerMBean serverStatistics = new AccountMapController(AccountMap.getInstance());
//        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//        ObjectName name = new ObjectName("ServerManager:type=AccountServerController");
//        mbs.registerMBean(serverStatistics, name);


        HibernateUtil hibernateUtil = new HibernateUtil();
        SessionFactory sessionFactory = hibernateUtil.getSessionFactory();
        DBService dbService = new DBServiceImpl(sessionFactory);
        WebSocketService webSocketService = new CustomWebSocketService(dbService);

        final MessageSystem messageSystem = new MessageSystem();

        final Thread accountServiceThread = new Thread(new AccountMap(dbService,messageSystem));
        accountServiceThread.setDaemon(true);
        accountServiceThread.setName("Account Map");


        ServiceWrapper serviceWrapper = new ServiceWrapper(dbService,messageSystem);
        GameFactory gameFactory = new GameFactory(dbService);

        Server server = new Server(port);

        Router router = new Router(serviceWrapper);
        AdminServlet adminServlet = new AdminServlet(serviceWrapper);

        SocketServlet socketServlet = new SocketServlet(webSocketService, gameFactory);


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

        accountServiceThread.start();
        server.start();
        server.join();

    }
}
