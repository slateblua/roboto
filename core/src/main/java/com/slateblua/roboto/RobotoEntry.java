package com.slateblua.roboto;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.slateblua.roboto.data.Segment;
import com.slateblua.roboto.data.Tuple;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.slateblua.roboto.render.RobotHandRenderer;

public class RobotoEntry extends ApplicationAdapter {
    private Stage stage;
    private RobotHandRenderer renderer;
    private Solver solver;
    private Array<Segment> segments;
    private Tuple target;

    private VisLabel errorLabel;
    private VisSelectBox<String> selectBox;
    private VisSlider jointSlider;

    private CCDSolver ccdSolver;
    private FABRIKSolver fabrikSolver;

    @Override
    public void create() {
        if (!VisUI.isLoaded()) {
            VisUI.load();
        }

        stage = new Stage(new FitViewport(640, 480));

        segments = new Array<>();
        resetSegments(3);

        createSolvers();

        target = new Tuple(400, 300);
        solver = ccdSolver;
        renderer = new RobotHandRenderer();

        createUI();

        Gdx.input.setInputProcessor(stage);
    }

    private void createSolvers () {
        ccdSolver = new CCDSolver();
        fabrikSolver = new FABRIKSolver();
    }

    private void resetSegments(int count) {
        segments.clear();
        for (int i = 0; i < count; i++) {
            segments.add(new Segment(150f / (float) Math.sqrt(count)));
        }
    }

    private void createUI() {
        final VisTable table = new VisTable();
        table.setFillParent(true);
        table.top().left().pad(10);

        selectBox = new VisSelectBox<>();

        selectBox.setItems("CCD", "FABRIK");

        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectBox.getSelected().equals("CCD")) {
                    solver = ccdSolver;
                } else {
                    solver = fabrikSolver;
                }
            }
        });


        jointSlider = new VisSlider(1, 20, 1, false);
        jointSlider.setValue(3);

        final VisLabel jointLabel = new VisLabel("Joints: 3");

        jointSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int count = (int) jointSlider.getValue();
                jointLabel.setText("Joints: " + count);
                resetSegments(count);
                // The solvers hold a reference to the segments list, but we might want to ensure they are aware.
                // Re-creating the solver is safer.
                if (selectBox.getSelected().equals("CCD")) {
                    solver = new CCDSolver();
                } else {
                    solver = new FABRIKSolver();
                }
            }
        });

        errorLabel = new VisLabel("Error: 0.0");

        table.add(new VisLabel("Solver:" )).left();
        table.add(selectBox).width(100).spaceLeft(80).left().row();
        table.add(jointLabel).left().padTop(10).width(80);
        table.add(jointSlider).width(150).spaceLeft(80).left().row();
        table.add(errorLabel).left().padTop(10).colspan(2);

        table.setName("HUD");
        stage.addActor(table);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1f);

        if (Gdx.input.isTouched()) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            stage.getViewport().unproject(mousePos);
            Actor hit = stage.hit(mousePos.x, mousePos.y, true);
            if (hit == null || !hit.isDescendantOf(stage.getRoot().findActor("HUD"))) {
                target = new Tuple(mousePos.x, mousePos.y);
            }
        }

        solver.solve(target, segments);

        Segment endEffector = segments.get(segments.size - 1);

        float error = endEffector.getEnd().dst(target);

        errorLabel.setText(String.format("Error: %.2f", error));

        renderer.render(segments, target);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if (width <= 0 || height <= 0) return;
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        renderer.dispose();
        VisUI.dispose();
    }
}
