package com.mygdx.projectMeta.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.projectMeta.GameWorld;
import com.mygdx.projectMeta.PhysicsEngine;
import com.mygdx.projectMeta.systems.*;
import com.mygdx.projectMeta.utils.Constants;

/**
 * Created by Dan on 4/22/2015.
 */
public class GameScreen implements Screen {

    //public static GameStage stage;
    private GameWorld gameWorld;
    private PhysicsEngine physicsEngine;
    private Engine gameEngine;
    private SpriteBatch batch;

    public GameScreen() {

        //ProjectMeta.assets.load("images/marsAttacks/torso.png", Texture.class);
        //ProjectMeta.assets.finishLoading();

        setupSpriteBatch();
        //stage = new GameStage();

        physicsEngine = new PhysicsEngine();
        gameEngine = new Engine();

        gameEngine.addSystem(new RenderingSystem(batch, physicsEngine.getWorld()));
        gameEngine.addSystem(new CameraSystem());
        gameEngine.addSystem(new InputSystem());
        gameEngine.addSystem(new PlayerMovementSystem());
        gameEngine.addSystem(new AnimationSystem());
        gameEngine.addSystem(new StateSystem());
        gameEngine.addSystem(new PlayerSystem());
        gameEngine.addSystem(new TorsoMovementSystem());
        gameEngine.addSystem(new FurnitureMovementSystem());

        gameEngine.getSystem(InputSystem.class).setCamera(gameEngine.getSystem(RenderingSystem.class).getCamera());

        gameWorld = new GameWorld(gameEngine, physicsEngine.getWorld());
        gameWorld.createWorld();
    }

    private void setupSpriteBatch()
    {
        batch = new SpriteBatch();
        Vector3 position = new Vector3(0, 0, 0f);
        Quaternion rotation = new Quaternion();
        Vector3 scaleVec = new Vector3(Constants.SCALE, Constants.SCALE, 1);
        Matrix4 transform = new Matrix4(position, rotation, scaleVec);
        batch.setTransformMatrix(transform);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        //stage.draw();
        //stage.act(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
        }
    }

    public void update(float delta)
    {
        physicsEngine.update(delta);
        gameEngine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        //ProjectMeta.assets.unload("skins/uiskin.json");
        //ProjectMeta.assets.unload("icons/icons.atlas");
        //stage.dispose();
    }
}
