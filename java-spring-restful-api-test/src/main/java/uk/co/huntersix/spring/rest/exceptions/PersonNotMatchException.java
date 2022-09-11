package uk.co.huntersix.spring.rest.exceptions;

public class PersonNotMatchException extends Exception{
    public PersonNotMatchException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "PersonNoMatchException{}";
    }
}
