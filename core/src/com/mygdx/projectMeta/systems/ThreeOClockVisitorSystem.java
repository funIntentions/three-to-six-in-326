package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.projectMeta.components.*;

/**
 * Created by Dan on 8/20/2015.
 */
public class ThreeOClockVisitorSystem extends IteratingSystem {
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<AnimationComponent> animationMapper;
    private ComponentMapper<SteeringComponent> steeringMapper;

    public ThreeOClockVisitorSystem () {
        super(Family.getFor(ThreeOClockVisitorComponent.class, PhysicsComponent.class));

        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        steeringMapper = ComponentMapper.getFor(SteeringComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

        AnimationComponent animationComponent = animationMapper.get(entity);
        StateComponent stateComponent = stateMapper.get(entity);
        SteeringComponent steeringComponent = steeringMapper.get(entity);

        Animation animation = animationComponent.animations.get(stateComponent.get());

        if (animation.getAnimationDuration() <= stateComponent.time) {
            if (stateComponent.get() == ThreeOClockVisitorComponent.SPAWNING) {
                steeringComponent.wanderOn = true;
                stateComponent.set(ThreeOClockVisitorComponent.MOVING);
            }
        }
    }
}
