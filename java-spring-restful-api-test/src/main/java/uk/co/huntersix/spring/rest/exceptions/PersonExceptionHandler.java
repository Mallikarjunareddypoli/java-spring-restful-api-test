package uk.co.huntersix.spring.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PersonExceptionHandler {


    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<String> handlePersonNotFoundException(PersonNotFoundException personNotFoundException) {

        return new ResponseEntity<String>(personNotFoundException.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(PersonNotMatchException.class)
    public ResponseEntity<String> handlePersonNoMatchException(PersonNotMatchException personNoMatchException) {

        return new ResponseEntity<String>(personNoMatchException.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(PersonAlreadyExistException.class)
    public ResponseEntity<String> handlePersonNoMatchException(PersonAlreadyExistException personExistException) {

        return new ResponseEntity<String>(personExistException.getMessage(), HttpStatus.CONFLICT);

    }


}
