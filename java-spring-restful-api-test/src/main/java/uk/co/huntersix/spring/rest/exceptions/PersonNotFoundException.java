package uk.co.huntersix.spring.rest.exceptions;

public class PersonNotFoundException extends Exception{

    public PersonNotFoundException(String message) {
        super(message);

    }

    @Override
    public String toString() {
        return "PersonNotFoundException{}";
    }
}
