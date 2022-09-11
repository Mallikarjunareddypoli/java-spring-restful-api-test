package uk.co.huntersix.spring.rest.referenceData;

import org.junit.Before;
import org.junit.Test;
import uk.co.huntersix.spring.rest.exceptions.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonDataServiceTest {
    private PersonDataService personDataService;

    @Before
    public void setUp(){
        personDataService = new PersonDataService();
    }

    @Test
    public void shouldReturnPersonWithFirstAndLastName() throws PersonNotFoundException {
        Person person = personDataService.findPerson("Smith", "Mary");
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName().equals("Mary"));
        assertThat(person.getLastName().equals("Smith"));
    }
    @Test
    public void shouldNotReturnPersonByIncorrectFirstName() throws PersonNotFoundException {
        Person person = personDataService.findPerson("Smith", "Test");
        assertThat(person).isNull();
    }
    @Test
    public void shouldNotReturnPersonByIncorrectLastName() throws PersonNotFoundException {
        Person person = personDataService.findPerson("Test", "Mary");
        assertThat(person).isNull();
    }
    @Test
    public void shouldReturnPersonsBySurname1() throws PersonNotFoundException {
        List<Person> listPerson = personDataService.findPersonsBySurname("Archer");
        assertThat(listPerson.size()).isEqualTo(1);

    }
    @Test
    public void shouldReturnPersonsBySurname2()  {
        List<Person> listPerson = personDataService.findPersonsBySurname("Brown");
        assertThat(listPerson.size()).isEqualTo(2);

    }
    @Test
    public void shouldNotReturnPersonsBySurname()  {
        List<Person> listPerson = personDataService.findPersonsBySurname("Test");
        assertThat(listPerson.size()).isEqualTo(0);

    }
    @Test
    public void shouldAddPersons() {
        Long id = personDataService.addPerson(new Person("TestFirstName","TestLastName"));
        assertThat(id).isNotNull();
        Person person = personDataService.findPerson("TestLastName", "TestFirstName");
        assertThat(person.getId()).isEqualTo(id);

    }
    @Test
    public void shouldReturnPersonExist() {
        Long id = personDataService.addPerson(new Person("Peter", "Brown"));
        assertThat(id).isEqualTo(0L);
    }

}

