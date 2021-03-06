package com.mygdx.projectMeta.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Dan on 4/22/2015.
 */
public class Constants {
    public static final int APP_WIDTH = 900;
    public static final int APP_HEIGHT = 900;
    public static final int VIEWPORT_WIDTH = 20;
    public static final int PIXELS_PER_UNIT = 16;
    public static final float CAMERA_ZOOM = 1.9f;
    public static final float SCALE = 1.0f / Constants.PIXELS_PER_UNIT;

    public static final Vector2 WORLD_GRAVITY = new Vector2(0, 0);

    public static final float FRICTION_FORCE = 0.5f;

    public static final float BATHTUB_X = 6;
    public static final float BATHTUB_Y = 6;
    public static final float BATHTUB_HEIGHT = 1.4f;
    public static final float BATHTUB_WIDTH = 4f;

    public static final float COUCH_X = 16;
    public static final float COUCH_Y = 16;
    public static final float COUCH_HEIGHT = 1.4f;
    public static final float COUCH_WIDTH = 3.6f;
    public static final float COUCH_DAMPING = 5f;
    public static final float COUCH_ANGULAR_DAMPING = 75f;
    public static final float COUCH_DENSITY = 1f;
    public static final float COUCH_LINEAR_FORCE = 1f;

    public static final float TV_X = 16;
    public static final float TV_Y = 24;
    public static final float TV_HEIGHT = 1.1f;
    public static final float TV_WIDTH = 1.1f;
    public static final float TV_DAMPING = 5f;
    public static final float TV_ANGULAR_DAMPING = 500f;
    public static final float TV_DENSITY = 1f;
    public static final float TV_LINEAR_FORCE = 1f;

    public static final float DUCKY_X = 20;
    public static final float DUCKY_Y = 16;
    public static final float DUCKY_HEIGHT = 0.4f;
    public static final float DUCKY_WIDTH = 0.4f;
    public static final float DUCKY_DAMPING = 5f;
    public static final float DUCKY_DENSITY = 0.1f;
    public static final float DUCKY_LINEAR_FORCE = 1f;

    public static final float TOILET_X = 6;
    public static final float TOILET_Y = 15;
    public static final float TOILET_HEIGHT = 1f;
    public static final float TOILET_WIDTH = 1f;

    public static final float PLAYER_X = 10;
    public static final float PLAYER_Y = 10;
    public static final float PLAYER_WIDTH = 2.3f;
    public static final float PLAYER_HEIGHT = 1.7f;
    public static final float PLAYER_DAMPING = 5f;
    public static final float PLAYER_ANGULAR_DAMPING = 100f;
    public static final float PLAYER_DENSITY = 0.5f;
    public static final float PLAYER_LINEAR_FORCE = 1f;

    public static final float DEMON_X = 12;
    public static final float DEMON_Y = 16;
    public static final float DEMON_WIDTH = 3f;
    public static final float DEMON_HEIGHT = 3f;
    public static final float DEMON_ANGULAR_DAMPING = 5f;
    public static final float DEMON_DAMPING = 5f;
    public static final float DEMON_DENSITY = 0.5f;
    public static final float DEMON_LINEAR_FORCE = 1f;
    public static final float ANT_DEMON_WIDTH = 1.5f;
    public static final float ANT_DEMON_HEIGHT = 1.5f;

    public static final float PLAYER_LEGS_ANGULAR_CHANGE = 4f; // degree rotation per time step
    public static final float PLAYER_TORSO_ANGULAR_CHANGE = 6.5f; // degree rotation per time step
    public static final float PLAYER_TORSO_DIFFERENCE = MathUtils.PI / 4.0f;

    public static final int FORWARD = 1;
    public static final int BACKWARD = 1 << 2;
    public static final int LEFT = 1 << 3;
    public static final int RIGHT = 1 << 4;

    public static final float MAX_VELOCITY = 4.8f;
    public static final float MAX_VELOCITY_SQR = MAX_VELOCITY * MAX_VELOCITY;
}
