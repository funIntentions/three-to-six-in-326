package com.mygdx.projectMeta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.projectMeta.screens.GameScreen;

public class ProjectMeta extends Game {

    public static final AssetManager assets = new AssetManager();

    @Override
    public void create() {
        Assets.load();
        Texture.setAssetManager(assets);
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        assets.dispose();

    }
}
