package com.mygdx.projectMeta;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.projectMeta.utils.BodyUtils;
import com.mygdx.projectMeta.utils.Constants;
import com.mygdx.projectMeta.utils.MapBodyManager;
import com.mygdx.projectMeta.utils.WorldUtils;

/**
 * Created by Dan on 7/18/2015.
 */
public class PhysicsEngine implements ContactListener
{
    private World world;
    private RayHandler rayHandler;
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    public PhysicsEngine() {
        world = WorldUtils.createWorld();
        world.setContactListener(this);
        rayHandler = new RayHandler(world);

        MapBodyManager mapBodyManager = new MapBodyManager(world, Constants.PIXELS_PER_UNIT, null, 1);
        mapBodyManager.createPhysics(Assets.map, "physics");
    }

    public void update(float deltaTime) {
        accumulator += deltaTime;

        while (accumulator >= deltaTime) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
    }

    public World getWorld() {
        return world;
    }

    public RayHandler getRayHandler()
    {
        return rayHandler;
    }

    public void beginContact(Contact contact) {

    }

    public void endContact(Contact contact) {

    }

    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public void dispose()
    {
        rayHandler.dispose();
        world.dispose();
    }
}
