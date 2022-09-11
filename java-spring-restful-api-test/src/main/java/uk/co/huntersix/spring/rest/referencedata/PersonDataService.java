package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.exceptions.PersonNotMatchException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static List<Person> PERSON_DATA;

    static {
        PERSON_DATA = new ArrayList<Person>();
        PERSON_DATA.add(new Person("Mary", "Smith"));
        PERSON_DATA.add(new Person("Brian", "Archer"));
        PERSON_DATA.add(new Person("Collin", "Brown"));
        PERSON_DATA.add(new Person("Brian", "Smith"));
        PERSON_DATA.add(new Person("Peter", "Brown"));
    }

    public Person findPerson(String lastName, String firstName) {

        return PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst().orElse(null);

    }

    public List<Person> findPersonsBySurname(String lastName)  {

        return PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    public Long addPerson(Person person) {

        if (findNonMatchPerson(person)) {
            PERSON_DATA.add(person);
        } else {
            return 0L;
        }

        return person.getId();
    }

    private boolean findNonMatchPerson(Person person)  {

        return PERSON_DATA.stream().noneMatch(p -> p.getLastName().equalsIgnoreCase(person.getLastName())
                && p.getFirstName().equalsIgnoreCase(person.getFirstName()));
    }
}
