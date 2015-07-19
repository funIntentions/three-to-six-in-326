package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.projectMeta.components.BathtubComponent;
import com.mygdx.projectMeta.components.InputComponent;
import com.mygdx.projectMeta.components.StateComponent;
import com.mygdx.projectMeta.components.TriggerComponent;

import javax.swing.plaf.nimbus.State;

/**
 * Created by Dan on 7/18/2015.
 */
public class BathtubSystem extends IteratingSystem {

    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TriggerComponent> tm;
    private ComponentMapper<InputComponent> im;

    public BathtubSystem()
    {
        super(Family.getFor(BathtubComponent.class,
                StateComponent.class,
                TriggerComponent.class,
                InputComponent.class));

        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TriggerComponent.class);
        im = ComponentMapper.getFor(InputComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        StateComponent stateComponent = sm.get(entity);
        TriggerComponent triggerComponent = tm.get(entity);
        InputComponent inputComponent = im.get(entity);

        if (triggerComponent.triggered
                && stateComponent.get() != BathtubComponent.STATE_RUNNING
                && stateComponent.get() != BathtubComponent.STATE_DRAINING
                && inputComponent.actionInput)
        {
            if (stateComponent.get() == BathtubComponent.STATE_RAN)
            {
                stateComponent.set(BathtubComponent.STATE_DRAINING);
            }
            else
            {
                stateComponent.set(BathtubComponent.STATE_RUNNING);
            }
        }
    }
}
