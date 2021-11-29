package com.conor.FantasyMap.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Map {
    private List<NamedPoint> points;
    private int width;
    private int height;
    private double scale;

}
