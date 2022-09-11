package uk.co.huntersix.spring.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uk.co.huntersix.spring.rest.utils.JsonUtlis.asJsonString;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.co.huntersix.spring.rest.constants.PersonConstants;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Mary"))
                .andExpect(jsonPath("lastName").value("Smith"));
    }
    @Test
    public void shouldReturnPersonNotFound() throws Exception {
        String errorMessage="Person Not Found";
        when(personDataService.findPerson(any(),any()))
                .thenReturn(null);
        MvcResult result= this.mockMvc.perform(get("/person/test/mary")).andReturn();
        assertThat(result).isNotNull();
        MockHttpServletResponse response=result.getResponse();
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.equals(PersonConstants.PERSON_NOT_FOUND));

    }
    @Test
    public void shouldReturnListOfMultiplePersonsBySurname() throws Exception {
        List<Person> expectedList=new ArrayList<Person>();
        expectedList.add(new Person("Mary", "Smith"));
        expectedList.add(new Person("Brian", "Archer"));
        expectedList.add(new Person("Collin", "Smith"));
        expectedList.add(new Person("Brian", "Smith"));

        when(personDataService.findPersonsBySurname(any())).thenReturn(expectedList);

        this.mockMvc.perform(get("/person/Smith"))
                .andExpect(status().isOk())
                .andExpect(response ->
                {
                    String jsonResp=response.getResponse().getContentAsString();
                    ObjectMapper objectMapper= new ObjectMapper();
                    List<Person> actualPersonList=objectMapper.readValue(jsonResp,List.class);
                    assertEquals(expectedList.size(),actualPersonList.size());
                });
    }
    @Test
    public void shouldReturnOnePersonBySurname() throws Exception {
        List<Person> expectedList=new ArrayList<Person>();
        expectedList.add(new Person("Mary", "Smith"));


        when(personDataService.findPersonsBySurname(any())).thenReturn(expectedList);

        this.mockMvc.perform(get("/person/Smith"))
                .andExpect(status().isOk())
                .andExpect(response ->
                {
                    String jsonResp=response.getResponse().getContentAsString();
                    ObjectMapper objectMapper= new ObjectMapper();
                    List<Person> actualPersonList=objectMapper.readValue(jsonResp,List.class);
                    assertEquals(expectedList.size(),actualPersonList.size());
                });
    }
    @Test
    public void shouldReturnNotMatchBySurname() throws Exception {

        when(personDataService.findPersonsBySurname(any())).thenReturn(new ArrayList<Person>());

        MvcResult result= this.mockMvc.perform(get("/person/test")).andReturn();
        assertThat(result).isNotNull();
        MockHttpServletResponse response=result.getResponse();
        assertThat(response).isNotNull();
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
        assertThat(response.equals(PersonConstants.PERSON_NOT_FOUND_WITH_SURNAME));

    }

    @Test
    public void shouldAddPerson() throws Exception {

        Person person = new Person("Alister","Cook");
        when(personDataService.addPerson(any())).thenReturn(10L);
        this.mockMvc.perform(post("/person") .content(asJsonString(person))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(personDataService).addPerson(Mockito.any(Person.class));
    }

    @Test
    public void shouldReturnPersonAlreadyExist() throws Exception {

        Person person=new Person("Mary","Smith");
        when(personDataService.addPerson(person)).thenReturn(0L);
        MvcResult result = this.mockMvc.perform(post("/person") .content(asJsonString(person))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(result).isNotNull();
        MockHttpServletResponse response=result.getResponse();
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(409);
        assertThat(response.equals(PersonConstants.PERSON_DETAILS_EXIST));

    }

}