package util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogFactory {
    // Здесь возможно подошел бы измененный паттерн Context, чтобы в случае отсутствие такого логера его создавать,
    // а запрашивать его по имени. Но мне не хотелось делать гибким логирование из-за проблем наименований
    private static LogFactory instance = null;
    private static Logger mainLogger = null; // Логгер не файла main, а просто основной
    private static Logger dbLogger = null;
    private static Logger apiLogger = null;
    private static Logger sessionLogger = null;
    private static Logger servletLogger = null;

    public static synchronized LogFactory getInstance(){
        if (instance == null){
            instance = new LogFactory();
        }
        return instance;
    }

    private LogFactory(){}

    public Logger getMainLogger(){
        return singletonFactory(mainLogger, "Main Log");
    }

    public Logger getDBLogger(){
        return singletonFactory(dbLogger, "DataBase Log");
    }

    public Logger getApiLogger(){
        return singletonFactory(apiLogger, "Api Log");
    }

    public Logger getSessionLogger(){
        return singletonFactory(sessionLogger, "Session Log");
    }

    public Logger getServletLogger(){
        return singletonFactory(servletLogger, "Servlet Log");
    }

    private Logger singletonFactory(Logger result,String name){
        if (result == null){
            result = LogManager.getLogger(name);
        }
        return result;
    }
}
