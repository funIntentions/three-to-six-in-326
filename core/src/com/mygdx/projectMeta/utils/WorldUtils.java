package com.mygdx.projectMeta.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.projectMeta.box2d.EntityUserData;
import com.mygdx.projectMeta.box2d.HandUserData;

/**
 * Created by Dan on 4/22/2015.
 */
public class WorldUtils {
    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

    public static Body createPlayer(World world, Entity entity) {
        Body mainBody = createDynamicOvalBody(world, Constants.PLAYER_X, Constants.PLAYER_Y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, Constants.PLAYER_DAMPING, Constants.PLAYER_ANGULAR_DAMPING, Constants.PLAYER_DENSITY, entity);

        PolygonShape hand = new PolygonShape();
        hand.setAsBox(0.2f, 0.2f, new Vector2(0.9f, 0.7f), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = hand;
        fixtureDef.density = Constants.PLAYER_DENSITY;
        fixtureDef.isSensor = true;

        Fixture handSensorFixture = mainBody.createFixture(fixtureDef);
        handSensorFixture.setUserData(new HandUserData());
        hand.dispose();

        return mainBody;
    }

    public static Body createCouch(World world, Entity entity) {
        return createDynamicBoxBody(world, Constants.COUCH_X, Constants.COUCH_Y, Constants.COUCH_WIDTH, Constants.COUCH_HEIGHT, Constants.COUCH_DAMPING, Constants.COUCH_ANGULAR_DAMPING, Constants.COUCH_DENSITY, entity);
    }

    public static Body createTV(World world, Entity entity) {
        return createDynamicBoxBody(world, Constants.TV_X, Constants.TV_Y, Constants.TV_WIDTH, Constants.TV_HEIGHT, Constants.TV_DAMPING, Constants.TV_ANGULAR_DAMPING, Constants.TV_DENSITY, entity);
    }

    public static Body createDemon(World world, Entity entity) {
        return createDynamicOvalBody(world, Constants.DEMON_X, Constants.DEMON_Y, Constants.DEMON_WIDTH, Constants.DEMON_HEIGHT, Constants.DEMON_DAMPING, Constants.DEMON_ANGULAR_DAMPING, Constants.DEMON_DENSITY, entity);
    }

    public static Body createDynamicOvalBody(World world, float x, float y, float width, float height, float damping, float angularDamping, float density, Entity entity) {
        int segments = 8; //(the more the more precise shape is, but the more time it takes to do collision detection)
        float segment = (float) (2.0f * Math.PI / (float) segments);

        Vector2 vertices[] = new Vector2[segments];
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;

        for (int i = 0; i < segments; i++) {
            vertices[i] = new Vector2((float) (halfWidth * Math.cos(segment * i)), (float) (halfHeight * Math.sin(segment * i)));
        }

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        Body body = createDynamicBody(world, shape, x, y, damping, angularDamping, density, entity);

        shape.dispose();
        return body;
    }

    public static Body createDynamicBoxBody(World world, float x, float y, float width, float height, float damping, float angularDamping, float density, Entity entity) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        Body body = createDynamicBody(world, shape, x, y, damping, angularDamping, density, entity);
        shape.dispose();

        return body;
    }

    public static Body createDynamicBody(World world, Shape shape, float x, float y, float damping, float angularDamping, float density, Entity entity) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.linearDamping = damping;
        bodyDef.angularDamping = angularDamping;
        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = Constants.FRICTION_FORCE;
        body.createFixture(fixtureDef).setUserData(new EntityUserData(entity));
        body.resetMassData();

        return body;
    }

    public static Body createDucky(World world, Entity entity) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.DUCKY_WIDTH, Constants.DUCKY_HEIGHT);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.DUCKY_X, Constants.DUCKY_Y));
        bodyDef.linearDamping = Constants.DUCKY_DAMPING;
        bodyDef.angularDamping = Constants.DUCKY_DAMPING;
        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.DUCKY_DENSITY;
        fixtureDef.friction = Constants.FRICTION_FORCE;
        body.createFixture(fixtureDef).setUserData(new EntityUserData(entity));
        body.resetMassData();
        shape.dispose();

        return body;
    }

    public static Body createStaticFurniture(World world, float x, float y, float width, float height) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(x, y));
        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        body.resetMassData();
        shape.dispose();

        return body;
    }
}
