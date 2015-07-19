package com.mygdx.projectMeta.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.projectMeta.box2d.RunnerUserData;
import com.mygdx.projectMeta.components.FurnitureComponent;
import com.mygdx.projectMeta.components.TextComponent;
import com.mygdx.projectMeta.components.TransformComponent;

/**
 * Created by Dan on 4/22/2015.
 */
public class WorldUtils {
    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

    public static Body createPlayer (World world)
    {
        int segments = 8; //(the more the more precise shape is, but the more time it takes to do collision detection)
        float segment = (float)(2.0f * Math.PI / (float)segments);

        Vector2 vertices[] = new Vector2[segments];
        float halfWidth = Constants.PLAYER_WIDTH/2.0f;
        float halfHeight = Constants.PLAYER_HEIGHT/2.0f;

        for (int i = 0; i < segments; i++)
        {
            vertices[i] = new Vector2((float)(halfWidth * Math.cos(segment * i)),(float)(halfHeight * Math.sin(segment * i)));
        }

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.PLAYER_X, Constants.PLAYER_Y));
        bodyDef.linearDamping = Constants.PLAYER_DAMPING;
        bodyDef.angularDamping = Constants.PLAYER_DAMPING;
        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.PLAYER_DENSITY;
        fixtureDef.friction = Constants.FRICTION_FORCE;
        body.createFixture(fixtureDef);
        body.resetMassData();
        body.setUserData(new RunnerUserData());
        shape.dispose();
        return body;
    }

    public static Body createCouch (World world)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.COUCH_WIDTH, Constants.COUCH_HEIGHT);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.COUCH_X, Constants.COUCH_Y));
        bodyDef.linearDamping = Constants.COUCH_DAMPING;
        bodyDef.angularDamping = Constants.COUCH_DAMPING;
        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.COUCH_DENSITY;
        fixtureDef.friction = Constants.FRICTION_FORCE;
        body.createFixture(fixtureDef);
        body.resetMassData();
        body.setUserData(new RunnerUserData());
        shape.dispose();

        return body;
    }

    public static Body createTV (World world)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.TV_WIDTH, Constants.TV_HEIGHT);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.TV_X, Constants.TV_Y));
        bodyDef.linearDamping = Constants.TV_DAMPING;
        bodyDef.angularDamping = Constants.TV_DAMPING;
        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.TV_DENSITY;
        fixtureDef.friction = Constants.FRICTION_FORCE;
        body.createFixture(fixtureDef);
        body.resetMassData();
        body.setUserData(new RunnerUserData());
        shape.dispose();

        return body;
    }

    public static Body createStaticFurniture(World world, float x, float y, float width, float height)
    {
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
        body.setUserData(new RunnerUserData());
        shape.dispose();

        return body;
    }
}
