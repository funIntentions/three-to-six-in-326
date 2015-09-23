package com.mygdx.projectMeta.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.projectMeta.Assets;
import com.mygdx.projectMeta.GameWorld;
import com.mygdx.projectMeta.PhysicsEngine;
import com.mygdx.projectMeta.systems.*;
import com.mygdx.projectMeta.utils.Constants;

/**
 * Created by Dan on 4/22/2015.
 */
public class GameScreen implements Screen {

    private Stage stage = new Stage(new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT));
    private Table buttonTable = new Table();
    private Table textTable = new Table();
    private Viewport viewport;

    private Skin skin = new Skin(Gdx.files.internal("skins/menuSkin.json"),
            new TextureAtlas(Gdx.files.internal("skins/menuSkin.pack")));

    private TextButton buttonResume = new TextButton("Resume", skin),
            buttonExit = new TextButton("Menu", skin);

    private Label title = new Label("Pause", skin);
    private Label timeLabel = new Label("Time" , skin);

    private GameWorld gameWorld;
    private PhysicsEngine physicsEngine;
    private RenderingSystem renderingSystem;
    private InputSystem inputSystem;
    private Engine gameEngine;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private SpriteBatch textBatch;
    private boolean paused = false;
    private float time = 0;

    ThreeOClockVisitorSystem threeOClockVisitorSystem;

    public GameScreen() {

        setupSpriteBatch();
        setupCamera();
        textBatch = new SpriteBatch();
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_WIDTH, camera);

        physicsEngine = new PhysicsEngine();
        gameEngine = new Engine();

        renderingSystem = new RenderingSystem(batch, camera, physicsEngine.getWorld(), physicsEngine.getRayHandler());
        inputSystem = new InputSystem();
        gameEngine.addSystem(renderingSystem);
        gameEngine.addSystem(new TextRenderingSystem(batch));
        gameEngine.addSystem(new TextSystem());
        gameEngine.addSystem(new CameraSystem());
        gameEngine.addSystem(inputSystem);
        gameEngine.addSystem(new PlayerMovementSystem());
        gameEngine.addSystem(new AnimationSystem());
        gameEngine.addSystem(new StateSystem());
        gameEngine.addSystem(new PlayerSystem());
        gameEngine.addSystem(new TorsoSystem());
        gameEngine.addSystem(new FurnitureMovementSystem());
        gameEngine.addSystem(new TriggerSystem());
        gameEngine.addSystem(new BathtubSystem());
        gameEngine.addSystem(new ToiletSystem());
        gameEngine.addSystem(new PortalSystem());
        gameEngine.addSystem(new HoldingSystem(physicsEngine.getWorld()));
        gameEngine.addSystem(new SteeringSystem(physicsEngine.getWorld()));

        ContactSystem contactSystem = new ContactSystem(physicsEngine.getWorld());
        physicsEngine.getWorld().setContactListener(contactSystem);
        gameEngine.addSystem(contactSystem);

        gameEngine.getSystem(InputSystem.class).setCamera(camera);

        gameWorld = new GameWorld(gameEngine, physicsEngine.getWorld());
        gameWorld.createWorld();

        threeOClockVisitorSystem = new ThreeOClockVisitorSystem(gameWorld);
        gameEngine.addSystem(threeOClockVisitorSystem);

        Gdx.input.setInputProcessor(inputSystem.getInputAdapter());
    }

    private void setupSpriteBatch() {
        batch = new SpriteBatch();
        Vector3 position = new Vector3(0, 0, 0f);
        Quaternion rotation = new Quaternion();
        Vector3 scaleVec = new Vector3(Constants.SCALE, Constants.SCALE, 1);
        Matrix4 transform = new Matrix4(position, rotation, scaleVec);
        batch.setTransformMatrix(transform);
    }

    private void setupCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_WIDTH * (h / w));
        Vector3 position = new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.translate(position);
        camera.zoom = Constants.CAMERA_ZOOM;
    }

    @Override
    public void render(float delta) {
        update(delta);
    }

    public void update(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            pause();

        if (!paused) {
            time += delta;

            physicsEngine.update(delta);
            gameEngine.update(delta);

            if (threeOClockVisitorSystem.haveVisit(time)) {
                System.out.println("Phase Complete!");
            }

        } else {
            renderingSystem.update(delta);
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height);
        viewport.apply();
        renderingSystem.resize();
    }

    @Override
    public void show() {

        textTable.add(timeLabel).row();
        textTable.setFillParent(true);
        textTable.setVisible(false);
        stage.addActor(textTable);

        buttonResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resume();
            }
        });

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        });

        buttonTable.add(title).padBottom(40).row();
        buttonTable.add(buttonResume).size(150, 60).padBottom(20).row();
        buttonTable.add(buttonExit).size(150, 60).padBottom(20).row();

        buttonTable.setFillParent(true);
        buttonTable.setVisible(false);
        stage.addActor(buttonTable);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {
        paused = true;
        buttonTable.setVisible(true);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void resume() {
        paused = false;
        buttonTable.setVisible(false);
        Gdx.input.setInputProcessor(inputSystem.getInputAdapter());
    }

    @Override
    public void dispose() {
        Assets.dispose();
        physicsEngine.dispose();
    }
}
