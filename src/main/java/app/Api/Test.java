package app.Api;


import DAO.logic.CardLogic;
import DAO.logic.EffectLogic;
import app.templater.PageGenerator;
import org.json.JSONArray;
import org.json.JSONObject;
import service.DBService;
import util.LogFactory;
import util.MessageList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

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
                CardLogic card= dbService.getCardService().getCard(8);

                for (EffectLogic e: card.getEffects()){
                    JSONObject json = new JSONObject(e.getValue());
                    JSONArray result2 = (JSONArray)json.get("value");
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
