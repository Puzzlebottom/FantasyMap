package com.conor.FantasyMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FantasyMapApplication {

	public static void main(String[] args) {

		SpringApplication.run(FantasyMapApplication.class, args);
	}

}

//TODO: create a map generator @Service including all the map generation logic
//TODO: add isOrigin attr to Location
//TODO: return a map where origin is at 0,0 and scale is modified to contain all points
//TODO: hookup the new MapService logic to "/" endpoint