package com.conor.FantasyMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FantasyMapApplication {

	public static void main(String[] args) {

		SpringApplication.run(FantasyMapApplication.class, args);
	}

}

//TODO: create simple UI mockup
//TODO: Do something with overflow hours in course log entries
//TODO: Undo button DONE (needs test. where?)
//TODO: TDD that endpoint with validation for well-formed log entries
//TODO: examine sausages (java generics)
//TODO: finish UI
//TODO: stretch goal: use github actions to run the tests on every push (CI)