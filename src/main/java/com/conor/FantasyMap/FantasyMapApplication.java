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

//TODO: FIX create an endpoint to move the party by deltaHours deltaXY & create new log entry
//TODO: examine sausages (java generics)
//TODO: UI that moves the party -- hint: HTML forms with hidden inputs are a good way to make a button that does a thing.. POST endpoints should redirect to /
//TODO: stretch goal: use github actions to run the tests on every push (CI)