package br.com.fo2app.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fo2app.springboot.dao.PersonRepository;
import br.com.fo2app.springboot.model.Person;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository repository;
	
	public List<Person> list(){
		return repository.findAll();		
	}
	
	public Person fetch(String id) {
		return repository.findOne(id);		
	}
	
	public Person save(Person person) {
		return repository.save(person);
	}
	
	public void delete(String id) {
		repository.delete(id);
	}
	
}
