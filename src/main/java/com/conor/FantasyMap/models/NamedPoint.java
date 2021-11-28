package com.conor.FantasyMap.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class NamedPoint {
    private String name;
    private int xCoord;
    private int yCoord;
    private boolean isCentered = false;

    public NamedPoint setCoords(int x, int y) {
        this.setXCoord(x);
        this.setYCoord(-y);
        return this;
    }
}
