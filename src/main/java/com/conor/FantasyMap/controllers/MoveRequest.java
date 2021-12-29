package com.conor.FantasyMap.controllers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveRequest {
    private String direction;
    private int deltaHours;
}
