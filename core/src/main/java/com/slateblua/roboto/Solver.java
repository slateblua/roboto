package com.slateblua.roboto;

import com.badlogic.gdx.utils.Array;
import com.slateblua.roboto.data.Segment;
import com.slateblua.roboto.data.Tuple;

public interface Solver {
    // Gets a target position and returns a list of angles to reach it.
    void solve(final Tuple target, final Array<Segment> segment);

    void setBase(float x, float y);
}
