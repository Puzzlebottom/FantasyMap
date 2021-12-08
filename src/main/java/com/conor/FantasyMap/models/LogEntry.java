package com.conor.FantasyMap.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.awt.*;

@Entity
@Getter
@Setter
public class LogEntry {
    @GeneratedValue
    @Id
    private Long id;
    private double deltaX;
    private double deltaY;
    private int deltaHours;
}
