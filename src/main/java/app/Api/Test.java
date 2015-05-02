package app.Api;


import DAO.logic.EffectLogic;
import DAO.logic.UserLogic;
import app.templater.PageGenerator;
import service.DBService;
import util.LogFactory;
import util.MessageList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;

public class Test {
    public void test(HttpServletRequest request,
                       HttpServletResponse response, DBService dbService) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> body = new HashMap<>();
        try{
            result.put("status",200);
            if (request.getMethod().equalsIgnoreCase("GET")) {
                result.put("error", MessageList.Message.UsePost);
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            } else {
                List<EffectLogic> effectList = dbService.getEffectService().getAllEffects();
                Integer i = 0;
                for (EffectLogic e : effectList){
                    body.put(i.toString(),e.toString());
                    i++;
                }
            }
            result.put("response", body);
            response.getWriter().println(PageGenerator.getJson(result));
        } catch (Exception e){
            LogFactory.getInstance().getLogger(this.getClass()).error("Test/test", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
