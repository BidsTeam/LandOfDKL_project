package app;

import DataBase.Controller.User;
import app.logic.FightFinder;
import app.templater.PageGenerator;
import org.json.simple.JSONObject;

import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Router extends HttpServlet {

    private String login = "";

    FightFinder fightFinder = new FightFinder();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
//        Pattern p = Pattern.compile("(:\\/\\/.*?)\\/(.*)");
//        Matcher m = p.matcher(request.getRequestURL());
//        String url = "";
//        while(m.find()){
//            url = m.group();
//        }
        String url = request.getServletPath();
        try {
            System.out.println(url);
            String[] urlParts = url.split("/");
            Class<?> cls = Class.forName("app.Controller." + urlParts[1].substring(0, 1).toUpperCase() + urlParts[1].substring(1));
            Object obj = cls.newInstance();
            Class[] paramTypes = new Class[] {HttpServletRequest.class,HttpServletResponse.class};
            Method method = cls.getMethod(urlParts[2], paramTypes);
            Object[] args = new Object[] {request, response};
            method.invoke(obj, args) ;
        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getMessage()+" In Router");
        }
        return;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String url = request.getServletPath();
        try {
            System.out.println(url);
            String[] urlParts = url.split("/");
            Class<?> cls = Class.forName("app.Controller."+urlParts[1].substring(0, 1).toUpperCase() + urlParts[1].substring(1));
            Object obj = cls.newInstance();
            Class[] paramTypes = new Class[] {HttpServletRequest.class,HttpServletResponse.class};
            Method method = cls.getMethod(urlParts[2], paramTypes);
            Object[] args = new Object[] {request, response};
            method.invoke(obj, args) ;
        } catch (Exception e){
            System.err.println(e.getMessage()+" In Router");
            e.printStackTrace();
        }
        return;
    }
}