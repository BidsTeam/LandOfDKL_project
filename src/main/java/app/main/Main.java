package app.main;

import app.servlets.AdminServlet;
import app.servlets.Router;
import app.servlets.SocketServlet;
import app.AccountCache.AccountCache;
import app.AccountCache.AccountCacheController;
import app.AccountCache.AccountCacheControllerMBean;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Main {
    public static void main(String[] args) throws Exception {
        //Frontend frontend = new Frontend();
        //todo Основные вопрос:
        //todo 1) Как сделать так, чтобы первый запрос к БД не был таким долгим. Т.е все необходимые оптимизации происходили при старте сервера
        //todo 2) Проблема с кодировкой с hibernate validator. исплоьзоваться i18n при каждом методе не хочется (как-то через бины решается)
        //todo 3) Проблема с timestamp см. logic/User/User()
        //todo 4) Какие-нибудь архитектурные советы
        Router router = new Router();
        String portString = "8080";
        if (args.length >= 1) {
            portString = args[0];
        }

        int port = Integer.valueOf(portString);
        System.out.append("Starting at port: ").append(portString).append('\n');


        AccountCacheControllerMBean serverStatistics = new AccountCacheController(AccountCache.getInstance());
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ServerManager:type=AccountServerController");
        mbs.registerMBean(serverStatistics, name);

        Server server = new Server(port);

        AdminServlet adminServlet = new AdminServlet();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        SocketServlet socketServlet = new SocketServlet();



        context.addServlet(new ServletHolder(adminServlet), "/admin/");
        context.addServlet(new ServletHolder(router), "/api/*");
        context.addServlet(new ServletHolder(socketServlet), "/main/*");

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
