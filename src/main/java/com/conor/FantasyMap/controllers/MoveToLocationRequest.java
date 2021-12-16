package com.conor.FantasyMap.controllers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveToLocationRequest {
    private String destinationName;
    private int deltaHours;
}
