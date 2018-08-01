package br.com.fo2app.springboot.test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.fo2app.springboot.dao.PersonRepository;
import br.com.fo2app.springboot.model.Person;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonResourceTest {

	private static final String resource = "persons";
	
	@Autowired
	private MockMvc mvc;

	@Autowired
	private PersonRepository repository;

	@SuppressWarnings("rawtypes")
	private HttpMessageConverter mappingJackson2HttpMessageConverter;	

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Test
	public void addTest() throws Exception {

		// Limpa a base de testes
		this.repository.deleteAll();

		List<Person> Persons = new ArrayList<Person>();
		// Nomes Viris
		Persons.add(new Person("Álvaro", 13));
		Persons.add(new Person("Cosme", 15));
		Persons.add(new Person("Heitor", 21));
		Persons.add(new Person("Oswaldo", 18));
		Persons.add(new Person("Timóteo", 16));
		// Nomes Legais
		Persons.add(new Person("Lucca", 10));
		// Nomes Fracos
		Persons.add(new Person("Ben", 17));
		// Nomes para Bulling
		Persons.add(new Person("Judá", 12));
		Persons.add(new Person("Zoey", 13));

		Persons.forEach(Person -> {
			try {
				mvc.perform(MockMvcRequestBuilders.put("/" + resource).contentType(MediaType.APPLICATION_JSON)
						.content(json(Person))).andExpect(status().isCreated())
						.andExpect(content().string(notNullValue()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	@Test
	public void fetchTest() throws Exception {
		
		Person person = repository.findByName("Ben");

		mvc.perform(MockMvcRequestBuilders.get("/" + resource + "/" + person.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(302)).andExpect(content().string(notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ben"));
		
	}
	
	@Test
	public void listTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/" + resource + "/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(notNullValue()));
	}
	
	@Test
	public void updateTest() throws Exception {
		
		Person person = repository.findByName("Zoey");

		mvc.perform(MockMvcRequestBuilders.delete("/" + resource + "/" + person.getId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(notNullValue()));
	}

	@Test
	public void removeTest() throws Exception {
		
		Person person = repository.findByName("Judá");
		
		person.setAge(11);

		mvc.perform(MockMvcRequestBuilders.post("/" + resource + "/" + person.getId()).contentType(MediaType.APPLICATION_JSON)
			.content(json(person)))
			.andExpect(status().isOk()).andExpect(content().string(notNullValue()));
	}
	
	private String json(Object o) throws IOException {
		try {
			MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
			this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
			return mockHttpOutputMessage.getBodyAsString();
		} catch (Exception e) {
			throw e;
		}
	}

}
