package com.slateblua.roboto;

import com.badlogic.gdx.utils.Array;
import com.slateblua.roboto.data.Angle;
import com.slateblua.roboto.data.Tuple;

public interface Solver {
    // Gets a target position and returns a list of angles to reach it.
    Array<Angle> solve(final Tuple target);
}
