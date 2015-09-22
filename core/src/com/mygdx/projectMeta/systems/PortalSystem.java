package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.projectMeta.components.PortalComponent;
import com.mygdx.projectMeta.components.TransformComponent;

/**
 * Created by Dan on 9/21/2015.
 */
public class PortalSystem extends IteratingSystem {

private ComponentMapper<TransformComponent> transformMapper;

    private float time = 0;

    public PortalSystem() {
        super(Family.getFor(PortalComponent.class,
                TransformComponent.class));

        transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = transformMapper.get(entity);

        time += deltaTime * 4;

        transformComponent.scale.set(Math.abs(MathUtils.sin(time))/4 + 0.6f, Math.abs(MathUtils.cos(time))/4 + 0.6f);

        float totalRotation = transformComponent.rotation + deltaTime;
        while (totalRotation < -180 * MathUtils.degreesToRadians) totalRotation += 360 * MathUtils.degreesToRadians;
        while (totalRotation > 180 * MathUtils.degreesToRadians) totalRotation -= 360 * MathUtils.degreesToRadians;
        transformComponent.rotation = totalRotation;
    }
}
