package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.projectMeta.components.*;

/**
 * Created by Dan on 7/19/2015.
 */
public class TriggerSystem extends IteratingSystem
{
    private ComponentMapper<TriggerComponent> triggerMapper;
    private ComponentMapper<TransformComponent> transformMapper;

    public TriggerSystem()
    {
        super(Family.getFor(TriggerComponent.class,
                TransformComponent.class));

        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        triggerMapper = ComponentMapper.getFor(TriggerComponent.class);
    }

    public void processEntity(Entity entity, float deltaTime)
    {

    }
}
