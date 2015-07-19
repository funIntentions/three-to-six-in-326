package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.projectMeta.components.BathtubComponent;
import com.mygdx.projectMeta.components.StateComponent;
import com.mygdx.projectMeta.components.TriggerComponent;

import javax.swing.plaf.nimbus.State;

/**
 * Created by Dan on 7/18/2015.
 */
public class BathtubSystem extends IteratingSystem {

    private ComponentMapper<BathtubComponent> bm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TriggerComponent> tm;

    public BathtubSystem()
    {
        super(Family.getFor(BathtubComponent.class, StateComponent.class));

        bm = ComponentMapper.getFor(BathtubComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TriggerComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        BathtubComponent bathtubComponent = bm.get(entity);
        StateComponent stateComponent = sm.get(entity);
        TriggerComponent triggerComponent = tm.get(entity);

        if (triggerComponent.triggered && stateComponent.get() != BathtubComponent.STATE_RUNNING)
        {

        }
    }
}
