package com.conor.FantasyMap.models;

import org.junit.jupiter.api.Test;

import static com.conor.FantasyMap.models.CardinalDirection.W;
import static org.assertj.core.api.Assertions.assertThat;

class PointTest {
    @Test
    void fromOriginByVectorShouldReturnPointToWest() {
        Point point = Point.fromOriginByVector(new Point(0, 0), W, 100);
        assertThat(point.getX()).isEqualTo(-100);
        assertThat(point.getY()).isEqualTo(0);
    }
}