package com.mygdx.projectMeta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Dan on 7/18/2015.
 */
public class TriggerComponent extends Component {
    public float range = 0;
    public boolean triggered = false;
    public Entity triggerer = null;
}
