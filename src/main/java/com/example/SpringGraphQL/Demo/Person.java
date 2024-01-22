package com.example.SpringGraphQL.Demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Person {

	@Id
	private int id;
	private String name;
	private String mobile;
	private String email;
	private String Address;
}
