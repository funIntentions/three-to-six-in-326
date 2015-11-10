package com.mygdx.projectMeta.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Dan on 11/9/2015.
 */
public class BodyQueryCallback implements QueryCallback {
    public Array<Body> foundBodies = new Array<Body>();

    @Override
    public boolean reportFixture(Fixture fixture) {
        foundBodies.add(fixture.getBody());
        return true;
    }
}
