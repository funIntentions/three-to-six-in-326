package com.mygdx.projectMeta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Joint;

/**
 * Created by Dan on 8/18/2015.
 */
public class HandComponent extends Component {
    public Joint distanceJoint = null;
    public Entity entityBeingHeld = null;
}
