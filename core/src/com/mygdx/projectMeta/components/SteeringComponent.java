package com.mygdx.projectMeta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.mygdx.projectMeta.Ray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 7/25/2015.
 */
public class SteeringComponent extends Component {
    public Entity target = null;
    /*public Vector2 wanderTarget = null;
    public float wanderOffset = 0;
    public float wanderRadius = 0;
    public float wanderJitter = 0;
    public boolean wanderOn = false;*/
    public List<Ray> feelers = new ArrayList<Ray>();
    public boolean seekOn = false;
}
