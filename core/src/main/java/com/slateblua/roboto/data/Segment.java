package com.slateblua.roboto.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Segment {
    private float length;
    private Angle angle; // Relative to parent
    private Tuple start;  // Calculated during forward kinematics
    private Tuple end;    // Calculated during forward kinematics

    public Segment(float length) {
        this.length = length;
        this.angle = new Angle(0);
        this.start = new Tuple(0, 0);
        this.end = new Tuple(length, 0);
    }
}
