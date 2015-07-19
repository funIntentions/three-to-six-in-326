package com.mygdx.projectMeta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.mygdx.projectMeta.components.AnimationComponent;

import java.util.ArrayList;

/**
 * Created by Dan on 7/18/2015.
 */
public class Assets
{
    public static Map map;
    public static TextureRegion playerTorso;
    public static TextureRegion couch;
    public static TextureRegion toilet;
    public static TextureRegion tv;
    public static Animation playerWalking;
    public static Animation playerStill;
    public static Texture playerTexture;
    public static Texture walkingTexture;
    public static Texture couchTexture;
    public static Texture toiletTexture;
    public static Texture tvTexture;

    public static Texture loadTexture(String file)
    {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load()
    {
        playerTexture = loadTexture("images/pjDude.png");
        couchTexture = loadTexture("images/couch.png");
        toiletTexture = loadTexture("images/toilet.png");
        tvTexture = loadTexture("images/tv.png");
        walkingTexture = loadTexture("images/pjLegsSnug.png");

        couch = new TextureRegion(couchTexture, couchTexture.getWidth(), couchTexture.getHeight());
        toilet = new TextureRegion(toiletTexture, toiletTexture.getWidth(), toiletTexture.getHeight());
        tv = new TextureRegion(tvTexture, tvTexture.getWidth(), tvTexture.getHeight());
        playerTorso = new TextureRegion(playerTexture, playerTexture.getWidth(), playerTexture.getHeight());
        playerWalking = new Animation(0.18f,
                new TextureRegion(walkingTexture, 112, 0, 28, 51),
                new TextureRegion(walkingTexture, 84, 0, 28, 51),
                new TextureRegion(walkingTexture, 56, 0, 28, 51),
                new TextureRegion(walkingTexture, 28, 0, 28, 51),
                new TextureRegion(walkingTexture, 0, 0, 28, 51),
                new TextureRegion(walkingTexture, 0, 51, 28, 51),
                new TextureRegion(walkingTexture, 28, 51, 28, 51),
                new TextureRegion(walkingTexture, 56, 51, 28, 51),
                new TextureRegion(walkingTexture, 84, 51, 28, 51));

        playerWalking.setPlayMode(Animation.PlayMode.LOOP);
        playerStill = new Animation(0.2f, new TextureRegion(walkingTexture, 112, 0, 28, 51));
        map = new TmxMapLoader().load("apartment.tmx");
    }
}
