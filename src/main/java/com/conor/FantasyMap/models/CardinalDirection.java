package com.conor.FantasyMap.models;

import java.util.List;

public enum CardinalDirection {
    N, NE, E, SE, S, SW, W, NW;


    private static final List<Integer> cardinalDegrees = List.of(0, 45, 90, 135, 180, 225, 270, 315, 360);
    private static final List<CardinalDirection> cardinalValues = List.of(N, NE, E, SE, S, SW, W, NW, N);
    private static final int cardinalOffset = (360/cardinalValues.stream().distinct().toArray().length)/2;


    public static CardinalDirection ofAngle(int angle) {
        int regularAngle = (angle + 360) % 360;
        int index = cardinalDegrees.stream().filter(i -> regularAngle > (i - cardinalOffset)).toArray().length - 1;
        return cardinalValues.get(index);
    }

    public int toAngle() {
        return cardinalDegrees.get(cardinalValues.indexOf(this));
    }
}
