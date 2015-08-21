package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.projectMeta.components.CameraComponent;
import com.mygdx.projectMeta.components.TransformComponent;

/**
 * Created by Dan on 7/18/2015.
 */
public class CameraSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<CameraComponent> cm;

    public CameraSystem() {
        super(Family.getFor(CameraComponent.class));

        tm = ComponentMapper.getFor(TransformComponent.class);
        cm = ComponentMapper.getFor(CameraComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        CameraComponent camera = cm.get(entity);

        if (camera.target == null) {
            return;
        }

        TransformComponent target = tm.get(camera.target);

        if (target == null) {
            return;
        }

        float lerp = 6f;
        Vector3 position = camera.camera.position;
        //TODO: Bit of a hack, floating point number produced during lerp causes texture bleeding/gaps. Multiplying by a 1000, rounding, then dividing by 1000 fixes this... (just rounding makes the camera lag)
        position.x += (float) Math.round(1000f * (target.position.x - position.x) * lerp * deltaTime) / 1000f;
        position.y += (float) Math.round(1000f * (target.position.y - position.y) * lerp * deltaTime) / 1000f;
        camera.camera.position.set(position);
        camera.camera.update();
    }
}
