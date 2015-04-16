package util;


import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Set;

public class ValidatorSBT {
    public static HashMap<String,String> validate(Object object, javax.validation.Validator validator) {
        HashMap<String,String> result = new HashMap<>();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        System.out.println(object);
        System.out.println(String.format("Кол-во ошибок: %d",
                constraintViolations.size()));
        for (ConstraintViolation<Object> cv : constraintViolations) {
            System.out.println(String.format(
                    "Внимание, ошибка! property: [%s], value: [%s], message: [%s]",
                    cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));
            result.put(cv.getPropertyPath().toString(),cv.getMessage());
        }

        return result;
    }
}
