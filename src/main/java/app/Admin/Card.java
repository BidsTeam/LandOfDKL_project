package app.Admin;


import DAO.logic.CardLogic;
import DAO.logic.UserLogic;
import app.templater.PageGenerator;
import service.DBService;
import util.LogFactory;
import util.MessageList;
import util.ValidatorSBT;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;


public class Card {

//    public void create (HttpServletRequest request,
//                       HttpServletResponse response, DBService dbService) {
//        try {
//            HashMap<String, Object> result = new HashMap<>();
//            HashMap<String, Object> body = new HashMap<>();
//            try {
//                String name             = request.getParameter("name");
//                String effect           = "none";
//                String attack           =  request.getParameter("attack");
//                CardLogic.CardType type = CardLogic.CardType.fromString(request.getParameter("type"));
//                Integer is_playable     = 0;
//                CardLogic card = new CardLogic(name, effect, Integer.parseInt(attack), type, is_playable);
//                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//                HashMap<String,String> validateResult = ValidatorSBT.validate(card, validator);
//                if (validateResult.isEmpty()){
//                    try {
//                        if (dbService.getCardService().addCard(card)) {
//                            body.putAll(CardLogic.putAllUserInformation(card));
//                            result.put("status", 200);
//                            response.setStatus(HttpServletResponse.SC_OK);
//                        } else {
//                            result.put("error", MessageList.Message.CardNameAlreadyExists);
//                        }
//                    } catch (Exception e) {
//                        result.put("status", 500);
//                        LogFactory.getInstance().getLogger(this.getClass()).error("Admin/Card.create Error with create card", e);
//                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                    }
//                } else {
//                    result.put("status", 400);
//                    body.put("error",validateResult);
//                    response.setStatus(HttpServletResponse.SC_OK);
//                }
//            } catch (Exception e){
//                LogFactory.getInstance().getLogger(this.getClass()).error("Admin/Card.create", e);
//                response.setStatus(HttpServletResponse.SC_OK);
//                result.put("status", 500);
//                body.put("error", MessageList.Message.UnknownErrorOnServer);
//            }
//            result.put("response", body);
//            response.getWriter().println(PageGenerator.getJson(result));
//        } catch (java.io.IOException e){
//            LogFactory.getInstance().getLogger(this.getClass()).fatal("Admin/Card.create IO EXCEPTION", e);
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }

//    }
}
