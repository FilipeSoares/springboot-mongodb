package br.com.fo2app.springboot.rest;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fo2app.springboot.model.Person;
import br.com.fo2app.springboot.service.PersonService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/persons", produces="application/json")
public class PersonResource {
	
	@Autowired
	PersonService service;
	
	@GetMapping
	@ApiOperation(value="List Persons", httpMethod="GET", response=Person.class, responseContainer="List", code=200)
	public ResponseEntity<List<Person>> list(){
		return new ResponseEntity<List<Person>>(service.list(), HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}")
	@ApiOperation(value="Fetch Person By ID", httpMethod="GET", response=Person.class, code=302)
	public ResponseEntity<Person> fetchById(@PathVariable("id") String id){
		try {
			return new ResponseEntity<Person>(service.fetch(id), HttpStatus.FOUND);
		} catch (NullPointerException e) {
			return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<Person>(HttpStatus.BAD_GATEWAY);
		}
	}
	
	@PutMapping
	@ApiOperation(value="Create a new Person", httpMethod="PUT", response=Person.class, code=201)
	public ResponseEntity<Person> create(@RequestBody Person Person, UriComponentsBuilder ucBuilder){
		try {
			return new ResponseEntity<Person>(service.save(Person), HttpStatus.CREATED);
		} catch (ConstraintViolationException ex) {
			return new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<Person>(HttpStatus.BAD_GATEWAY);
		}
		
	}
	
	@PostMapping(value="/{id}")
	@ApiOperation(value="Update a exists Person", httpMethod="POST", response=Person.class, code=200)
	public ResponseEntity<Person> update(@PathVariable("id") String id, @RequestBody Person Person, UriComponentsBuilder ucBuilder){
		try {
			return new ResponseEntity<Person>(service.save(Person), HttpStatus.OK);
		} catch (ConstraintViolationException ex) {
			return new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<Person>(HttpStatus.BAD_GATEWAY);
		}
		
	}
	
	@DeleteMapping(value="/{id}")
	@ApiOperation(value="Remove Person", httpMethod="DELETE", code=200)
	public ResponseEntity<Void> remove(@PathVariable("id") String id){
		try {
			service.delete(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.BAD_GATEWAY);
		}
	}

}
