package com.slateblua.roboto.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tuple {
    private final float x;
    private final float y;

    public float dst(Tuple other) {
        float dx = x - other.x;
        float dy = y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float angleDeg(Tuple other) {
        return (float) Math.toDegrees(Math.atan2(other.y - y, other.x - x));
    }
}
