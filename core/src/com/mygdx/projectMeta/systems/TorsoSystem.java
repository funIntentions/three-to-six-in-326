package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.projectMeta.components.*;
import com.mygdx.projectMeta.utils.Constants;

/**
 * Created by Dan on 7/18/2015.
 */
public class TorsoSystem extends IteratingSystem
{
    private int previousState;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<TorsoComponent> cm;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<AnimationComponent> animationMapper;

    public TorsoSystem()
    {
        super(Family.getFor(TorsoComponent.class, TransformComponent.class, StateComponent.class, AnimationComponent.class));

        tm = ComponentMapper.getFor(TransformComponent.class);
        cm = ComponentMapper.getFor(TorsoComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
        previousState = PlayerComponent.STATE_STILL;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        TransformComponent positionComponent = tm.get(entity);
        TorsoComponent torsoComponent = cm.get(entity);
        StateComponent stateComponent = stateMapper.get(entity);
        AnimationComponent animationComponent = animationMapper.get(entity);

        Animation animation = animationComponent.animations.get(stateComponent.get());
        Entity target = torsoComponent.target;

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)
                && stateComponent.get() != PlayerComponent.STATE_GRAB)
        {
            previousState = stateComponent.get();
            stateComponent.set(PlayerComponent.STATE_GRAB);
        }
        else if (stateComponent.get() == PlayerComponent.STATE_GRAB
                && animation.getAnimationDuration() <= stateComponent.time)
        {
            stateComponent.set(previousState);
        }

        if (tm.has(target))
        {
            TransformComponent targetPositionComponent = tm.get(target);
            StateComponent targetStateComponent = stateMapper.get(target);

            if (stateComponent.get() != targetStateComponent.get() && stateComponent.get() != PlayerComponent.STATE_GRAB)
                stateComponent.set(targetStateComponent.get());

            float totalRotation = targetPositionComponent.desiredRotation - positionComponent.rotation;
            while ( totalRotation < -180 * MathUtils.degreesToRadians) totalRotation += 360 * MathUtils.degreesToRadians;
            while ( totalRotation >  180 * MathUtils.degreesToRadians) totalRotation -= 360 * MathUtils.degreesToRadians;
            float change = Constants.PLAYER_TORSO_ANGULAR_CHANGE * MathUtils.degreesToRadians;
            float newAngle = positionComponent.rotation + Math.min(change, Math.max(-change, totalRotation));

            newAngle = MathUtils.clamp(newAngle, targetPositionComponent.rotation - Constants.PLAYER_TORSO_DIFFERENCE, targetPositionComponent.rotation + Constants.PLAYER_TORSO_DIFFERENCE);

            positionComponent.position.set(targetPositionComponent.position.x, targetPositionComponent.position.y, positionComponent.position.z);
            positionComponent.rotation = newAngle;
        }
    }
}
