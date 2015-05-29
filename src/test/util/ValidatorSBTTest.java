package util;

import DAO.logic.UserLogic;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ValidatorSBTTest {

    @Test
    public void testValidate() throws Exception {
        UserLogic user = new UserLogic("", "", "test");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        HashMap<String,String> validateResult = UserLogic.validate(user, validator);
        assertFalse(validateResult.isEmpty());
    }

    @Test
    public void testValidateRight() throws Exception {
        UserLogic user = new UserLogic("user", "12345", "test@test.ru");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        HashMap<String, String> validateResult = UserLogic.validate(user, validator);
        assertTrue(validateResult.isEmpty());
    }
}