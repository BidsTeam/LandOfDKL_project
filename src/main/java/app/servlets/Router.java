package app.servlets;

import util.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;


public class Router extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        try {
            route(request, response);
        } catch (Exception e){
            LogFactory.getInstance().getServletLogger().error("Router/doGet",e); // специально вынес логирование из метода route
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        try {
            route(request, response);
        } catch (Exception e){
            LogFactory.getInstance().getServletLogger().error("Router/doPost",e);
        }
    }
    private void route(HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        try {
            routeInvoke(urlParse(request.getRequestURI()),request,response);
        } catch (Exception e){
            throw new Exception(e);
        }
    }
    private String[] urlParse(String url){
        String[] urlBuf = url.split("/");
        String[] urlParts;
        if (urlBuf.length < 4){
            urlParts = new String[4];
            System.arraycopy(urlBuf,0,urlParts,0,urlBuf.length);
            urlParts[3] = "main";
        } else {
            urlParts = urlBuf;
        }
        return urlParts;
    }

    private void routeInvoke(String[] urlParts,HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        try {
            LogFactory.getInstance().getServletLogger().debug("Router.routeInvoke: app.Api." + urlParts[2].substring(0, 1).toUpperCase() + urlParts[2].substring(1));
            Class<?> cls = Class.forName("app.Api." + urlParts[2].substring(0, 1).toUpperCase() + urlParts[2].substring(1));
            Object obj = cls.newInstance();
            Class[] paramTypes = new Class[]{HttpServletRequest.class, HttpServletResponse.class};
            Method method = cls.getMethod(urlParts[3].toLowerCase(), paramTypes);
            Object[] args = new Object[]{request, response};
            method.invoke(obj, args);
        } catch (Exception e){
            throw new Exception(e);
        }

    }
}