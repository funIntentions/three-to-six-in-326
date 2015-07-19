package com.mygdx.projectMeta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Dan on 7/18/2015.
 */
public class TransformComponent extends Component
{
    public final Vector3 position = new Vector3();
    public final Vector2 scale = new Vector2(1.0f, 1.0f);
    public float rotation = 0.0f;
    public float desiredRotation = 0.0f;
}
