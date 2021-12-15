package com.conor.FantasyMap.models;

import org.junit.jupiter.api.Test;

import static com.conor.FantasyMap.models.CardinalDirection.*;
import static org.assertj.core.api.Assertions.assertThat;


class CardinalDirectionTest {

    @Test
    void ofAngleShouldReturnExactDirection() {
        assertThat(CardinalDirection.ofAngle(90)).isEqualTo(E);
    }

    @Test
    void ofAngleShouldReturnClosestDirection() {
        assertThat(CardinalDirection.ofAngle(11)).isEqualTo(N);
    }

    @Test
    void ofAngleShouldReturnCorrectDirectionWithNegativeInput() {
        assertThat(CardinalDirection.ofAngle(-361)).isEqualTo(N);
    }

    @Test
    void ofAngleShouldReturnCorrectDirectionWithInputGreaterThan360() {
        assertThat(CardinalDirection.ofAngle(384)).isEqualTo(NE);
    }

    @Test
    void toAngleShouldReturnAnAngle() {
        assertThat(W.toAngle()).isEqualTo(270);
    }

}