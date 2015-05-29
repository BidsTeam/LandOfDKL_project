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

    public void stop(HttpServletRequest request, HttpServletResponse response, DBService dbService) {
        System.exit(0);
    }
}
