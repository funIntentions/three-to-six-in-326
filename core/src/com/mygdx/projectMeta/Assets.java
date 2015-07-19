package com.mygdx.projectMeta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

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
    public static Animation bathtubDrained;
    public static Animation bathtubRan;
    public static Animation bathtubDraining;
    public static Animation bathtubRunning;
    public static Animation playerWalking;
    public static Animation playerStill;
    public static Texture playerTexture;
    public static Texture walkingTexture;
    public static Texture couchTexture;
    public static Texture toiletTexture;
    public static Texture tvTexture;
    public static Texture bathtubTexture;

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
        walkingTexture = loadTexture("images/pjLegsSnug.png"); // 28 x 51
        bathtubTexture = loadTexture("images/bathtubFilling.png"); // 135 x 51

        couch = new TextureRegion(couchTexture, couchTexture.getWidth(), couchTexture.getHeight());
        toilet = new TextureRegion(toiletTexture, toiletTexture.getWidth(), toiletTexture.getHeight());
        tv = new TextureRegion(tvTexture, tvTexture.getWidth(), tvTexture.getHeight());

        // Player
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

        // Bathtub
        bathtubRunning = new Animation(1f,
                new TextureRegion(bathtubTexture, 0, 1, 135, 51),
                new TextureRegion(bathtubTexture, 136, 1, 135, 51),
                new TextureRegion(bathtubTexture, 272, 1, 135, 51),
                new TextureRegion(bathtubTexture, 408, 1, 135, 51),
                new TextureRegion(bathtubTexture, 0, 52, 135, 51),
                new TextureRegion(bathtubTexture, 136, 52, 135, 51),
                new TextureRegion(bathtubTexture, 272, 52, 135, 51),
                new TextureRegion(bathtubTexture, 408, 52, 135, 51),
                new TextureRegion(bathtubTexture, 0, 103, 135, 51),
                new TextureRegion(bathtubTexture, 136, 103, 135, 51),
                new TextureRegion(bathtubTexture, 272, 103, 135, 51),
                new TextureRegion(bathtubTexture, 408, 103, 135, 51));

        bathtubDraining = new Animation(1f,
                new TextureRegion(bathtubTexture, 408, 103, 135, 51),
                new TextureRegion(bathtubTexture, 272, 103, 135, 51),
                new TextureRegion(bathtubTexture, 136, 103, 135, 51),
                new TextureRegion(bathtubTexture, 0, 103, 135, 51),
                new TextureRegion(bathtubTexture, 408, 52, 135, 51),
                new TextureRegion(bathtubTexture, 272, 52, 135, 51),
                new TextureRegion(bathtubTexture, 136, 52, 135, 51),
                new TextureRegion(bathtubTexture, 0, 52, 135, 51),
                new TextureRegion(bathtubTexture, 408, 1, 135, 51),
                new TextureRegion(bathtubTexture, 272, 1, 135, 51),
                new TextureRegion(bathtubTexture, 136, 1, 135, 51),
                new TextureRegion(bathtubTexture, 0, 1, 135, 51));

        bathtubDrained = new Animation(0.1f, new TextureRegion(bathtubTexture, 0, 0, 135, 51));
        bathtubRan = new Animation(0.1f, new TextureRegion(bathtubTexture, 408, 103, 135, 51));

        map = new TmxMapLoader().load("apartment.tmx");
    }
}
