package app.servlets;

import service.DBService;
import util.LogFactory;
import util.RouteHelper;
import util.ServiceWrapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;


public class Router extends HttpServlet {

    private ServiceWrapper serviceWrapper;


    public Router(ServiceWrapper serviceWrapper){
        this.serviceWrapper = serviceWrapper;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        try {
            route(request, response);
        } catch (Exception e){
            LogFactory.getInstance().getLogger(this.getClass()).error("Router/doGet",e); // специально вынес логирование из метода route
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        try {
            route(request, response);
        } catch (Exception e){
            LogFactory.getInstance().getLogger(this.getClass()).error("Router/doPost",e);
        }
    }
    private void route(HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        try {
            routeInvoke(RouteHelper.urlParse(request.getRequestURI()),request,response);
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    private void routeInvoke(String[] urlParts,HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        try {
            LogFactory.getInstance().getLogger(this.getClass()).debug("Router.routeInvoke: app.Api." + urlParts[2].substring(0, 1).toUpperCase() + urlParts[2].substring(1)+"/"+urlParts[3].toLowerCase());
            Class<?> cls = Class.forName("app.Api." + urlParts[2].substring(0, 1).toUpperCase() + urlParts[2].substring(1));
            Object obj = cls.newInstance();
            Class[] paramTypes = new Class[]{HttpServletRequest.class, HttpServletResponse.class, ServiceWrapper.class};
            Method method = cls.getMethod(urlParts[3].toLowerCase(), paramTypes);
            Object[] args = new Object[]{request, response, serviceWrapper};
            method.invoke(obj, args);
        } catch (Exception e){
            throw new Exception(e);
        }
    }
}