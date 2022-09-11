package uk.co.huntersix.spring.rest.exceptions;

public class PersonAlreadyExistException extends Exception{


    public PersonAlreadyExistException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "PersonExistException{}";
    }

}
