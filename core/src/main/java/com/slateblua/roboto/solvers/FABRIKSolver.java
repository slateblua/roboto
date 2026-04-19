package com.slateblua.roboto.solvers;

import com.badlogic.gdx.utils.Array;
import com.slateblua.roboto.Solver;
import com.slateblua.roboto.data.Angle;
import com.slateblua.roboto.data.Segment;
import com.slateblua.roboto.data.Tuple;

public class FABRIKSolver implements Solver {
    private static final int MAX_ITERATIONS = 100;
    private static final float TOLERANCE = 1.0f;
    private float baseX = 320;
    private float baseY = 100;

    @Override
    public void setBase(float x, float y) {
        this.baseX = x;
        this.baseY = y;
    }

    @Override
    public void solve(final Tuple target, final Array<Segment> segments) {
        if (segments.isEmpty()) return;

        float totalLength = 0;

        for (Segment s : segments) totalLength += s.getLength();

        final Tuple base = new Tuple(baseX, baseY);

        // Target is unreachable
        if (base.dst(target) > totalLength) {
            float dist = base.dst(target);
            for (int i = 0; i < segments.size; i++) {
                final Segment s = segments.get(i);
                float r = base.dst(target);
                float lambda = s.getLength() / r;
                // Simplified unreachable handling
                // This would need more complex logic for correct joint angle derivation in unreachable case
                // For now, let's just do the reachability steps
            }
        }

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            Segment last = segments.get(segments.size - 1);
            if (last.getEnd().dst(target) < TOLERANCE) break;

            // Backward pass
            Tuple currentTarget = target;
            for (int i = segments.size - 1; i >= 0; i--) {
                Segment s = segments.get(i);
                float r = s.getStart().dst(currentTarget);
                float lambda = s.getLength() / r;
                Tuple newStart = new Tuple(
                    (1 - lambda) * currentTarget.getX() + lambda * s.getStart().getX(),
                    (1 - lambda) * currentTarget.getY() + lambda * s.getStart().getY()
                );
                s.setEnd(currentTarget);
                s.setStart(newStart);
                currentTarget = newStart;
            }

            // Forward pass
            Tuple currentBase = base;
            for (int i = 0; i < segments.size; i++) {
                Segment s = segments.get(i);
                float r = s.getEnd().dst(currentBase);
                float lambda = s.getLength() / r;
                Tuple newEnd = new Tuple(
                    (1 - lambda) * currentBase.getX() + lambda * s.getEnd().getX(),
                    (1 - lambda) * currentBase.getY() + lambda * s.getEnd().getY()
                );
                s.setStart(currentBase);
                s.setEnd(newEnd);
                currentBase = newEnd;
            }
        }

        // Update angles based on resulting positions
        float prevAngle = 0;
        for (Segment s : segments) {
            float worldAngle = (float) Math.toDegrees(Math.atan2(s.getEnd().getY() - s.getStart().getY(), s.getEnd().getX() - s.getStart().getX()));
            float relativeAngle = worldAngle - prevAngle;
            s.setAngle(new Angle(relativeAngle));
            prevAngle = worldAngle;
        }
    }
}
