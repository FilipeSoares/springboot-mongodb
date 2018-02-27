package br.com.fo2app.springboot.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fo2app.springboot.model.Person;

@Repository
public interface PersonRepository extends MongoRepository<Person, String>{
	
	Person findByName(@Param("name") String name);

}
