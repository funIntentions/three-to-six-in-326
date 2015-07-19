package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.projectMeta.components.TorsoComponent;
import com.mygdx.projectMeta.components.TransformComponent;
import com.mygdx.projectMeta.utils.Constants;

/**
 * Created by Dan on 7/18/2015.
 */
public class TorsoMovementSystem extends IteratingSystem
{

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<TorsoComponent> cm;

    public TorsoMovementSystem()
    {
        super(Family.getFor(TorsoComponent.class, TransformComponent.class));

        tm = ComponentMapper.getFor(TransformComponent.class);
        cm = ComponentMapper.getFor(TorsoComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        TransformComponent positionComponent = tm.get(entity);
        TorsoComponent torsoComponent = cm.get(entity);

        Entity target = torsoComponent.target;

        if (tm.has(target))
        {
            TransformComponent targetPositionComponent = tm.get(target);

            float totalRotation = targetPositionComponent.desiredRotation - positionComponent.rotation;
            while ( totalRotation < -180 * MathUtils.degreesToRadians) totalRotation += 360 * MathUtils.degreesToRadians;
            while ( totalRotation >  180 * MathUtils.degreesToRadians) totalRotation -= 360 * MathUtils.degreesToRadians;
            float change = Constants.PLAYER_TORSO_ANGULAR_CHANGE * MathUtils.degreesToRadians;
            float newAngle = positionComponent.rotation + Math.min(change, Math.max(-change, totalRotation));

            newAngle = MathUtils.clamp(newAngle, targetPositionComponent.rotation - Constants.PLAYER_TORSO_DIFFERENCE, targetPositionComponent.rotation + Constants.PLAYER_TORSO_DIFFERENCE);

            positionComponent.position.set(targetPositionComponent.position);
            positionComponent.rotation = newAngle;
        }
    }
}
