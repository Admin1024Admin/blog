package xin.l024.blog.util;

import org.apache.tomcat.util.buf.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字段验证异常
 */
public class ConstrainViolationExceptionHandler {
    public static String getMessage(ConstraintViolationException e){
        List<String> msgList = new ArrayList<>();
        for(ConstraintViolation<?> constraintViolation:e.getConstraintViolations()){
            msgList.add(constraintViolation.getMessage());
        }
        String message = String.join(",",msgList);
        return message;
    }
}
