package uk.co.huntersix.spring.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.huntersix.spring.rest.constants.PersonConstants;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnPersonDetails() throws Exception {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/smith/mary",
                        String.class
                )
        ).contains("Mary");
    }
    @Test
    public void shouldReturnPersonNotFound() throws Exception {

        String message=this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/test/mary",
                String.class);
        assertThat(message).isNotNull();
        assertThat(message).isEqualTo(PersonConstants.PERSON_NOT_FOUND);
    }
    @Test
    public void shouldReturnListOfPersonBySurname() throws Exception {
        List<Person> personList= this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/smith",
                List.class);
        assertThat(personList).isNotEmpty();
        assertThat(personList.size()).isGreaterThan(0);

    }
    @Test
    public void shouldReturnNotMatchBySurnameMessage() throws Exception {

        String message= this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/test",
                String.class
        );
        assertThat(message).isNotNull();
        assertThat(message).isEqualTo(PersonConstants.PERSON_NOT_FOUND_WITH_SURNAME);
    }
    @Test
    public void shouldAddPerson() throws Exception {

        Person person=new Person("FirstNameTest","LastNameTest");
        person.setId(null);
        Person actualPerson=this.restTemplate.postForObject(
                "http://localhost:" + port + "/person",person,Person.class

        );
        assertThat(person).isNotNull();
        assertThat(person.getFirstName().equals(actualPerson.getFirstName()));
        assertThat(person.getLastName().equals(actualPerson.getLastName()));

    }
    @Test
    public void shouldReturnPersonAlreadyExist() throws Exception {

        Person person=new Person("Mary","Smith");
        String actualPerson=this.restTemplate.postForObject(
                "http://localhost:" + port + "/person",person,String.class

        );
        assertThat(actualPerson).isNotNull();
        assertThat(PersonConstants.PERSON_DETAILS_EXIST.equals(actualPerson));
    }
}