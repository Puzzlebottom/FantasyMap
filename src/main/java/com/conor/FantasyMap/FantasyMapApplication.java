package com.conor.FantasyMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FantasyMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(FantasyMapApplication.class, args);
	}

}

//TODO: Do something with overflow hours in course log entries
//TODO: finish UI
//TODO: stretch goal: use github actions to run the tests on every push (CI)
//TODO: Fix Error when undo is clicked with no log entries in db
//TODO: Fix scaling when party is off the map (save current coords as location?)
//TODO: Scale origin marker relative to map scale
//TODO: Implement pace of travel (Not MVP, but...)

