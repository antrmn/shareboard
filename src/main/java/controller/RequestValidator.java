package controller;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RequestValidator {

    private final List<String> errors;
    private final HttpServletRequest request;
    private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$"); //non contempla overflow?
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("^(-)?(0|[1-9]\\d+)\\.\\d+$");

    public RequestValidator(HttpServletRequest request){
        this.errors = new ArrayList<>();
        this.request = request;
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    public List<String> getErrors(){
        return errors;
    }

    private boolean gatherError(boolean condition, String message){
        if(condition){
            return true;
        }else{
            errors.add(message);
            return false;
        }
    }

    private boolean required(String value){
        return value!=null && !value.isBlank();
    }

    public boolean assertMatch(String value, Pattern regexp, String msg){
        String param = request.getParameter(value);
        boolean condition = required(param) && regexp.matcher(param).matches();
        return gatherError(condition, msg);
    }

    public boolean assertInt(String value, String msg){
        return assertMatch(value, INT_PATTERN, msg);
    }

    public boolean assertDouble(String value, String msg){
        return assertMatch(value, DOUBLE_PATTERN, msg);
    }

    public boolean assertEmail(String value, String msg){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9.!_]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
        return assertMatch(value, pattern, msg);
    }

    public boolean assertInts(String value, String msg){
        String[] params = request.getParameterValues(value);
        boolean allInt = Arrays.stream(params).allMatch(param->INT_PATTERN.matcher(param).matches());
        return gatherError(allInt, msg);
    }

    public boolean assertSize(String first, String second, String msg){
        String[] firstList = request.getParameterValues(first);
        String[] secondList = request.getParameterValues(second);
        return gatherError(firstList.length == secondList.length, msg);
    }

}
