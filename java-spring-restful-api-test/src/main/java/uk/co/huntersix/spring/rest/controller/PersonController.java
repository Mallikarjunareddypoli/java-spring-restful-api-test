package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.constants.PersonConstants;
import uk.co.huntersix.spring.rest.exceptions.PersonAlreadyExistException;
import uk.co.huntersix.spring.rest.exceptions.PersonNotFoundException;
import uk.co.huntersix.spring.rest.exceptions.PersonNotMatchException;
import uk.co.huntersix.spring.rest.exceptions.PersonAlreadyExistException;
import uk.co.huntersix.spring.rest.exceptions.PersonNotFoundException;
import uk.co.huntersix.spring.rest.exceptions.PersonNotMatchException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public Person person(@PathVariable(value = "lastName") String lastName,
                         @PathVariable(value = "firstName") String firstName) throws PersonNotFoundException {

        Person person = personDataService.findPerson(lastName, firstName);
        if (person == null)
            throw new PersonNotFoundException(PersonConstants.PERSON_NOT_FOUND);

        return person;
    }

    @GetMapping("/person/{lastName}")
    public List<Person> getListOfPersonsWithSurname(@PathVariable(value = "lastName") String lastName) throws PersonNotMatchException



            , PersonNotFoundException {

        List<Person> listOfPersons = personDataService.findPersonsBySurname(lastName);
        if (listOfPersons.size() == 0)
            throw new PersonNotFoundException(PersonConstants.PERSON_NOT_FOUND_WITH_SURNAME);

        return listOfPersons;
    }

    @PostMapping("/person")
    public Person addPerson(@RequestBody Person person) throws PersonAlreadyExistException {

        Long id = personDataService.addPerson(person);
        if (id == 0L)
            throw new PersonAlreadyExistException(PersonConstants.PERSON_DETAILS_EXIST);
        return person;
    }
}