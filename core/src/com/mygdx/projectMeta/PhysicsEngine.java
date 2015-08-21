package com.mygdx.projectMeta;

import box2dLight.RayHandler;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.projectMeta.utils.Constants;
import com.mygdx.projectMeta.utils.MapBodyManager;
import com.mygdx.projectMeta.utils.WorldUtils;

/**
 * Created by Dan on 7/18/2015.
 */
public class PhysicsEngine {
    private World world;
    private RayHandler rayHandler;
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    public PhysicsEngine() {
        world = WorldUtils.createWorld();
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

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public void dispose() {
        rayHandler.dispose();
        world.dispose();
    }
}
