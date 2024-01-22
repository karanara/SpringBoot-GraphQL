package com.example.SpringGraphQL.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringGraphQL.Demo.Person;
import com.example.SpringGraphQL.Repository.PersonRepository;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

@RestController
public class PersonController {

	@Autowired
	private PersonRepository personRepository;
	
	@GetMapping("/retreive-person")
	public Person getPersonByEmail(String mail) {
		return personRepository.findByEmail(mail);
	}
	@Autowired
	private ResourceLoader resourceLoader;



	private GraphQL graphQL;
	
	@PostConstruct
	public void loadSchema() throws IOException {
		InputStream inputStream = resourceLoader.getResource("classpath:person.graphqls").getInputStream();
		TypeDefinitionRegistry registry = new SchemaParser().parse(inputStream);
	    RuntimeWiring wiring = buildWiring();
	    GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
	    graphQL = GraphQL.newGraphQL(schema).build();
	}

	private RuntimeWiring buildWiring() {
		// TODO Auto-generated method stub
		DataFetcher<List<Person>> fetcher1 = data -> {
			return (List<Person>) personRepository.findAll();
		};

		DataFetcher<Person> fetcher2 = data -> {
			return personRepository.findByEmail(data.getArgument("email"));
		};
		return RuntimeWiring.newRuntimeWiring().type("Query",
		        typeWriting -> typeWriting.dataFetcher("getAllPerson", fetcher1).dataFetcher("findPerson", fetcher2))
		        .build();

	}
	
	@PostMapping("/getAll")
	public ResponseEntity<Object> getAll(@RequestBody String query){
		ExecutionResult result = graphQL.execute(query);
	    return new ResponseEntity<Object>(result,HttpStatus.OK);
	}
	
	@PostMapping("/getPersonByEmail")
	public ResponseEntity<Object> getPersonEmail(@RequestBody String query){
		ExecutionResult result = graphQL.execute(query);
	    return new ResponseEntity<Object>(result,HttpStatus.OK);	
	 }

	
}
