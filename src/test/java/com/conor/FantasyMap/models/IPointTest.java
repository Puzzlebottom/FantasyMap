package com.conor.FantasyMap.models;

import org.junit.jupiter.api.Test;

import static com.conor.FantasyMap.models.CardinalDirection.NE;
import static org.assertj.core.api.Assertions.assertThat;

class IPointTest {
    @Test
    void calculateBearingToShouldReturnCardinalDirection() {
        IPoint point = new IPoint() {
            @Override
            public double getX() {
                return 0;
            }

            @Override
            public double getY() {
                return 0;
            }

            @Override
            public void setX(double value) {

            }

            @Override
            public void setY(double value) {

            }
        };

        assertThat(point.calculateBearingTo(new Point(1, 1))).isEqualTo(NE);
    }

}