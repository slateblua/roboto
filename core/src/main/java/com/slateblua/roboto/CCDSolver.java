package com.slateblua.roboto;

import com.badlogic.gdx.utils.Array;
import com.slateblua.roboto.data.Angle;
import com.slateblua.roboto.data.Segment;
import com.slateblua.roboto.data.Tuple;

public class CCDSolver implements Solver {
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
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            updateForwardKinematics(segments);

            final Segment endEffector = segments.get(segments.size - 1);

            if (endEffector.getEnd().dst(target) < TOLERANCE) {
                break;
            }

            for (int j = segments.size - 1; j >= 0; j--) {
                updateForwardKinematics(segments);

                final Segment current = segments.get(j);
                Tuple endEffectorPos = segments.get(segments.size - 1).getEnd();

                float angleToEndEffector = current.getStart().angleDeg(endEffectorPos);
                float angleToTarget = current.getStart().angleDeg(target);

                float deltaAngle = angleToTarget - angleToEndEffector;
                float currentAngle = current.getAngle().getDegrees();
                current.setAngle(new Angle(currentAngle + deltaAngle));
            }
        }
    }

    private void updateForwardKinematics(final Array<Segment> segments) {
        float currentX = baseX;
        float currentY = baseY;
        float totalAngle = 0;

        for (final Segment segment : segments) {
            totalAngle += segment.getAngle().getDegrees();
            segment.setStart(new Tuple(currentX, currentY));

            float endX = currentX + (float) Math.cos(Math.toRadians(totalAngle)) * segment.getLength();
            float endY = currentY + (float) Math.sin(Math.toRadians(totalAngle)) * segment.getLength();

            segment.setEnd(new Tuple(endX, endY));
            currentX = endX;
            currentY = endY;
        }
    }
}
