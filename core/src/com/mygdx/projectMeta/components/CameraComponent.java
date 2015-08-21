package com.mygdx.projectMeta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;

/**
 * Created by Dan on 7/18/2015.
 */
public class CameraComponent extends Component {
    public Camera camera;
    public Entity target;
}
