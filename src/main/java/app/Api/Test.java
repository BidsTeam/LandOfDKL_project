package app.Api;


import DAO.logic.CardLogic;
import DAO.logic.EffectLogic;
import DAO.logic.UserLogic;
import app.templater.PageGenerator;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import service.DBService;
import util.LogFactory;
import util.MessageList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public class Test {
    public void test(HttpServletRequest request,
                       HttpServletResponse response, DBService dbService) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> body = new HashMap<>();
        Session session = dbService.getSession();
        try{
            result.put("status",200);
            if (request.getMethod().equalsIgnoreCase("GET")) {
                result.put("error", MessageList.Message.UsePost);
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            } else {
                List<CardLogic> cards= dbService.getCardService(session).getAllCardsInfo();
                for (CardLogic card : cards){
                    System.out.println(card.putAllCardInformation());
                }
//                dbService.getUserService(session).addUser(user);
            }
            result.put("response", body);
            response.getWriter().println(PageGenerator.getJson(result));
        } catch (Exception e){
            LogFactory.getInstance().getLogger(this.getClass()).error("Test/test", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            dbService.closeSession(session);
        }
    }
}
