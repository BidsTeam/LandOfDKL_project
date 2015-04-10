package util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class LogFactory {
    private static LogFactory instance = null;
    private static HashMap<String, Logger> context = new HashMap<>();

    public static synchronized LogFactory getInstance(){
        if (instance == null){
            instance = new LogFactory();
        }
        return instance;
    }

    private LogFactory(){}

    public Logger getLogger(Class<?> clazz){
        if(!context.containsKey(clazz.getName())){
            context.put(clazz.getName(), LogManager.getLogger(clazz.getName() + " Log"));
        }
        return context.get(clazz.getName());
    }
}
