package com.example.SpringGraphQL.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SpringGraphQL.Demo.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {
	
	
	Person findByEmail(String email);

}
