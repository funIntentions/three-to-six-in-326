package com.mygdx.projectMeta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Joint;

/**
 * Created by Dan on 7/19/2015.
 */
public class HoldableComponent extends Component {
    public Joint distanceJoint = null;
    public Entity holder = null;
    public Boolean held = false;
}
