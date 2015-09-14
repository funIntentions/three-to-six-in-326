package com.mygdx.projectMeta.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.projectMeta.utils.Constants;

/**
 * Created by Dan on 8/22/2015.
 */
public class MainMenuScreen implements Screen {

    private Stage stage = new Stage(new FitViewport(Constants.APP_WIDTH, Constants.APP_HEIGHT));
    private Table table = new Table();

    private Skin skin = new Skin(Gdx.files.internal("skins/menuSkin.json"),
            new TextureAtlas(Gdx.files.internal("skins/menuSkin.pack")));

    private String fullScreen = "Full Screen", windowed = "Windowed";

    private TextButton buttonPlay = new TextButton("Play", skin),
            buttonExit = new TextButton("Exit", skin),
            buttonFullScreen = new TextButton(windowed, skin);

    private Label title = new Label("3:00 to 6:00 in 326", skin);

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();

        stage.getBatch().begin();
        //stage.getBatch().draw(Assets.menuBackground, 0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.getBatch().end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void show() {

        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        buttonFullScreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, !Gdx.graphics.isFullscreen());

                if (Gdx.graphics.isFullscreen()) {
                    buttonFullScreen.setText(fullScreen);
                } else {
                    buttonFullScreen.setText(windowed);
                }
            }
        });

        table.add(title).padBottom(100).row();
        table.add(buttonPlay).size(150, 60).padBottom(20).row();
        table.add(buttonFullScreen).size(220, 60).padBottom(20).row();
        table.add(buttonExit).size(150, 60).padBottom(100).row();

        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
