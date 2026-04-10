package com.slateblua.roboto.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.slateblua.roboto.data.Segment;
import com.slateblua.roboto.data.Tuple;

public class RobotHandRenderer {
    private final ShapeRenderer shapeRenderer;

    public RobotHandRenderer() {
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(final Array<Segment> segments, final Tuple target) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw target
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(target.getX(), target.getY(), 5);

        // Draw segments
        for (int i = 0; i < segments.size; i++) {
            Segment segment = segments.get(i);
            shapeRenderer.setColor(Color.WHITE);

            shapeRenderer.rectLine(segment.getStart().getX(), segment.getStart().getY(),
                    segment.getEnd().getX(), segment.getEnd().getY(), 4);

            // Draw joints
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.circle(segment.getStart().getX(), segment.getStart().getY(), 5);
            if (i == segments.size - 1) {
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.circle(segment.getEnd().getX(), segment.getEnd().getY(), 5);
            }
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
