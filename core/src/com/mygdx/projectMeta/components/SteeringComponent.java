package com.mygdx.projectMeta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.projectMeta.Ray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 7/25/2015.
 */
public class SteeringComponent extends Component {
    public Entity target = null;
    public Vector2 wanderTarget = new Vector2();
    public float force = 300;
    public float wanderOffset = 10f;
    public float wanderRadius = 10f;
    public float wanderJitter = 20f;
    public boolean wanderOn = false;
    public List<Ray> feelers = new ArrayList<Ray>();
    public boolean seekOn = false;
}
