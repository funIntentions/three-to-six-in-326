package com.mygdx.projectMeta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.projectMeta.utils.Constants;

/**
 * Created by Dan on 7/18/2015.
 */
public class Assets
{
    public static Map map;

    public static TextureRegion couch;
    public static TextureRegion toilet;
    public static TextureRegion ducky;

    public static Animation bathtubDrained;
    public static Animation bathtubRan;
    public static Animation bathtubDraining;
    public static Animation bathtubRunning;
    public static Animation playerLegsWalking;
    public static Animation playerTorsoWalking;
    public static Animation playerLegsIdle;
    public static Animation playerTorsoIdle;
    public static Animation tvChannelStatic;

    public static Texture playerTexture;
    public static Texture legsWalkingTexture;
    public static Texture torsoWalkingTexture;
    public static Texture couchTexture;
    public static Texture toiletTexture;
    public static Texture bathtubTexture;
    public static Texture tvChannelTexture;
    public static Texture duckyTexture;
    public static Texture grabTexture;

    public static Sound slipperStepsSound;
    public static Sound bathtubRunningSound;
    public static Sound bathtubDrainingSound;

    public static BitmapFont journalFont;
    public static BitmapFont amaticFont;

    private static Texture loadTexture(String file)
    {
        return new Texture(Gdx.files.internal(file));
    }

    private static Sound loadSound(String file)
    {
        return Gdx.audio.newSound(Gdx.files.internal(file));
    }

    public static void load()
    {
        couchTexture = loadTexture("images/couch.png");
        toiletTexture = loadTexture("images/toilet.png");
        legsWalkingTexture = loadTexture("images/pjLegsWalk.png"); // 28 x 51
        bathtubTexture = loadTexture("images/bathtubFilling.png"); // 135 x 51
        tvChannelTexture = loadTexture("images/tvChannelStatic.png");
        torsoWalkingTexture = loadTexture("images/torsoWalk.png");
        duckyTexture = loadTexture("images/ducky.png");
        grabTexture = loadTexture("images/grabV1.png");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/journal.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 200;
        journalFont = generator.generateFont(parameter);
        journalFont.setScale(0.1f);
        journalFont.setColor(Color.DARK_GRAY);
        journalFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        generator.dispose();

        FreeTypeFontGenerator generator1 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AmaticSC-Regular.ttf"));
        amaticFont = generator1.generateFont(parameter);
        generator1.dispose();

        slipperStepsSound = loadSound("sounds/slipperSteps.mp3");
        bathtubRunningSound = loadSound("sounds/bathtubRunning.mp3");
        bathtubDrainingSound = loadSound("sounds/bathtubDraining.mp3");

        couch = new TextureRegion(couchTexture, couchTexture.getWidth(), couchTexture.getHeight());
        toilet = new TextureRegion(toiletTexture, toiletTexture.getWidth(), toiletTexture.getHeight());
        ducky = new TextureRegion(duckyTexture, duckyTexture.getWidth(), duckyTexture.getHeight());

        // TV
        tvChannelStatic = new Animation(0.2f,
                new TextureRegion(tvChannelTexture, 0, 0, 41, 42),
                new TextureRegion(tvChannelTexture, 41, 0, 41, 42),
                new TextureRegion(tvChannelTexture, 0, 42, 41, 42),
                new TextureRegion(tvChannelTexture, 41, 42, 41, 42));
        tvChannelStatic.setPlayMode(Animation.PlayMode.LOOP);

        // Player
        playerLegsWalking = new Animation(0.14f,
                new TextureRegion(legsWalkingTexture, 112, 0, 28, 51),
                new TextureRegion(legsWalkingTexture, 84, 0, 28, 51),
                new TextureRegion(legsWalkingTexture, 56, 0, 28, 51),
                new TextureRegion(legsWalkingTexture, 28, 0, 28, 51),
                new TextureRegion(legsWalkingTexture, 0, 0, 28, 51),
                new TextureRegion(legsWalkingTexture, 28, 0, 28, 51),
                new TextureRegion(legsWalkingTexture, 56, 0, 28, 51),
                new TextureRegion(legsWalkingTexture, 84, 0, 28, 51),
                new TextureRegion(legsWalkingTexture, 112, 0, 28, 51),
                new TextureRegion(legsWalkingTexture, 0, 51, 28, 51),
                new TextureRegion(legsWalkingTexture, 28, 51, 28, 51),
                new TextureRegion(legsWalkingTexture, 56, 51, 28, 51),
                new TextureRegion(legsWalkingTexture, 84, 51, 28, 51),
                new TextureRegion(legsWalkingTexture, 56, 51, 28, 51),
                new TextureRegion(legsWalkingTexture, 28, 51, 28, 51),
                new TextureRegion(legsWalkingTexture, 0, 51, 28, 51));
        playerLegsWalking.setPlayMode(Animation.PlayMode.LOOP);
        playerLegsIdle = new Animation(0.2f, new TextureRegion(legsWalkingTexture, 112, 0, 28, 51));

        playerTorsoWalking = new Animation(0.14f,
                new TextureRegion(torsoWalkingTexture, 0, 0, 40, 28),
                new TextureRegion(torsoWalkingTexture, 40, 0, 40, 28),
                new TextureRegion(torsoWalkingTexture, 80, 0, 40, 28),
                new TextureRegion(torsoWalkingTexture, 120, 0, 40, 28),
                new TextureRegion(torsoWalkingTexture, 160, 0, 40, 28),
                new TextureRegion(torsoWalkingTexture, 120, 0, 40, 28),
                new TextureRegion(torsoWalkingTexture, 80, 0, 40, 28),
                new TextureRegion(torsoWalkingTexture, 40, 0, 40, 28),
                new TextureRegion(torsoWalkingTexture, 0, 0, 40, 28),
                new TextureRegion(torsoWalkingTexture, 0, 28, 40, 28),
                new TextureRegion(torsoWalkingTexture, 40, 28, 40, 28),
                new TextureRegion(torsoWalkingTexture, 80, 28, 40, 28),
                new TextureRegion(torsoWalkingTexture, 120, 28, 40, 28),
                new TextureRegion(torsoWalkingTexture, 80, 28, 40, 28),
                new TextureRegion(torsoWalkingTexture, 40, 28, 40, 28),
                new TextureRegion(torsoWalkingTexture, 0, 28, 40, 28));
        playerTorsoWalking.setPlayMode(Animation.PlayMode.LOOP);
        playerTorsoIdle = new Animation(0.2f, new TextureRegion(torsoWalkingTexture, 0, 0, 39, 27));

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

    public static void dispose()
    {
        map.dispose();
        playerTexture.dispose();
        toiletTexture.dispose();
        legsWalkingTexture.dispose();
        torsoWalkingTexture.dispose();
        couchTexture.dispose();
        toiletTexture.dispose();
        tvChannelTexture.dispose();
        bathtubTexture.dispose();

        slipperStepsSound.dispose();
        bathtubDrainingSound.dispose();
        bathtubRunningSound.dispose();

        journalFont.dispose();
        amaticFont.dispose();
    }
}
