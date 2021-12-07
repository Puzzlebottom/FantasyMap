package com.conor.FantasyMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FantasyMapApplication {

	public static void main(String[] args) {

		SpringApplication.run(FantasyMapApplication.class, args);
	}

}

//TODO: move everything from MapService into Map.class, TDDing each unit as you go
//TODO: create simple UI mockup
//TODO: add TravelLog that computes party location from the entire travelLog (order by index)
//TODO: top entry in TravelLog asserts a location for the party rendered on the map
//TODO: create an endpoint to move the party by deltaHours deltaXY & create new log entry
//TODO: if there are no log entries, party is at origin
//TODO: otherwise party is at the sum of travel vectors
//TODO: examine sausages (java generics)

//TODO: Stretch Goal, UI that moves the party