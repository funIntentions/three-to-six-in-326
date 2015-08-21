package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.projectMeta.components.StateComponent;
import com.mygdx.projectMeta.components.TextComponent;
import com.mygdx.projectMeta.components.TransformComponent;
import com.mygdx.projectMeta.components.TriggerComponent;

/**
 * Created by Dan on 7/19/2015.
 */
public class TriggerSystem extends IteratingSystem {
    private ComponentMapper<TriggerComponent> triggerMapper;
    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<StateComponent> stateMapper;

    public TriggerSystem() {
        super(Family.getFor(TriggerComponent.class,
                TransformComponent.class));

        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        triggerMapper = ComponentMapper.getFor(TriggerComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
    }

    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = transformMapper.get(entity);
        TriggerComponent triggerComponent = triggerMapper.get(entity);

        if (triggerComponent.triggerer != null) {
            TransformComponent triggererTransformComponent = transformMapper.get(triggerComponent.triggerer);

            Vector3 pos1 = transformComponent.position;
            Vector3 pos2 = triggererTransformComponent.position;

            float distance = Vector3.dst(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);

            triggerComponent.triggered = (distance <= triggerComponent.range);

            if (triggerComponent.triggered && triggerComponent.actionTextEntity != null) {
                TransformComponent textTransformComponent = transformMapper.get(triggerComponent.actionTextEntity);
                StateComponent textStateComponent = stateMapper.get(triggerComponent.actionTextEntity);
                textTransformComponent.position.set(pos2.x + 1, pos2.y, textTransformComponent.position.z);
                textStateComponent.set(TextComponent.STATE_DISPLAYING);
            }
        }
    }
}
