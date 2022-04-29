package com.conor.FantasyMap.controllers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewLocationRequest {
    private String origin;
    private String newLocationName;
    private String direction;
    private int distance;

}
