package com.conor.FantasyMap;

import com.conor.FantasyMap.services.MapService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FantasyMapApplication {

	public static void main(String[] args) {

		SpringApplication.run(FantasyMapApplication.class, args);
	}

}

//TODO: generate SVG that has draws a dot at the coordinates of each location and puts text next to them; choose a fixed 1:1 world:svg grid like 400x400
//TODO: return the SVG in a templated HTML page using thymeleaf;
//TODO: create a map generator @Service including all the map generation logic