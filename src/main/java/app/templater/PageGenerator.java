package app.templater;

import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import util.LogFactory;
import util.MessageList;


import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class PageGenerator {
    private static final String HTML_DIR = "templates";
    private static final Configuration CFG = new Configuration();

    private static MessageList messageList = new MessageList(MessageList.LocaleRussia);


    public static String getJson(HashMap<String,Object> result){
        translate(result);
        Gson gson = new Gson();
        return gson.toJson(result);
    }

    private static void translate(HashMap<String,Object> map) {
        for (HashMap.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof MessageList.Message){
                entry.setValue(messageList.getText((MessageList.Message)value));
            } else if (value instanceof HashMap) {
                translate((HashMap) value);
            }
        }
    }
}
