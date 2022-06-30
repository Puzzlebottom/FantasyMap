package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.IPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class NamedPoint implements IPoint {
    private String name;
    private double x;
    private double y;

    @Override
    public void setX(double value) {

    }

    @Override
    public void setY(double value) {

    }
}
