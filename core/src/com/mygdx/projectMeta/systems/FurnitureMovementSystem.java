package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.projectMeta.components.FurnitureComponent;
import com.mygdx.projectMeta.components.PhysicsComponent;
import com.mygdx.projectMeta.components.TransformComponent;

/**
 * Created by Dan on 7/18/2015.
 */
public class FurnitureMovementSystem extends IteratingSystem
{
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<PhysicsComponent> pm;

    public FurnitureMovementSystem() {
        super(Family.getFor(TransformComponent.class, PhysicsComponent.class, FurnitureComponent.class));

        tm = ComponentMapper.getFor(TransformComponent.class);
        pm = ComponentMapper.getFor(PhysicsComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        PhysicsComponent physicsComponent = pm.get(entity);
        TransformComponent transformComponent = tm.get(entity);

        Vector2 bodyPosition = physicsComponent.body.getPosition();
        transformComponent.position.set(bodyPosition.x, bodyPosition.y, 0.0f);
        transformComponent.rotation = physicsComponent.body.getAngle();
    }
}
