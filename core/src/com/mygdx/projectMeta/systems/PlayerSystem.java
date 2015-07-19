package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.projectMeta.components.MovementComponent;
import com.mygdx.projectMeta.components.PlayerComponent;
import com.mygdx.projectMeta.components.StateComponent;
import com.mygdx.projectMeta.components.TransformComponent;

/**
 * Created by Dan on 7/18/2015.
 */
public class PlayerSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> pm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;

    public PlayerSystem() {
        super(Family.getFor(PlayerComponent.class,
                StateComponent.class,
                TransformComponent.class,
                MovementComponent.class));

        pm = ComponentMapper.getFor(PlayerComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PlayerComponent playerComponent = pm.get(entity);
        StateComponent stateComponent = sm.get(entity);
        MovementComponent movementComponent = mm.get(entity);

        if (stateComponent.get() == PlayerComponent.STATE_WALKING
                && movementComponent.movementInput == 0)
        {
            stateComponent.set(PlayerComponent.STATE_STILL);
        }
        else if (stateComponent.get() != PlayerComponent.STATE_WALKING
                && movementComponent.movementInput != 0)
        {
            stateComponent.set(PlayerComponent.STATE_WALKING);
        }
    }
}
