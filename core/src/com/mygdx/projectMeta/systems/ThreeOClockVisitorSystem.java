package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.mygdx.projectMeta.GameWorld;
import com.mygdx.projectMeta.components.ThreeOClockVisitorComponent;
import com.mygdx.projectMeta.components.TriggerComponent;

/**
 * Created by Dan on 8/20/2015.
 */
public class ThreeOClockVisitorSystem extends IteratingSystem {
    private ComponentMapper<TriggerComponent> triggerMapper;
    private Array<Entity> visitorsInPortal = new Array<Entity>();
    private final int numberOfVisitors = 1;
    private GameWorld gameWorld;

    public ThreeOClockVisitorSystem (GameWorld gameWorld) {
        super(Family.getFor(ThreeOClockVisitorComponent.class, TriggerComponent.class));

        triggerMapper = ComponentMapper.getFor(TriggerComponent.class);

        this.gameWorld = gameWorld;

        Entity portal = gameWorld.createPortal();
        gameWorld.createThreeOClockVisitor(portal);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

        TriggerComponent triggerComponent = triggerMapper.get(entity);

        int captured = -1;

        for (int visitorIndex = 0; visitorIndex < visitorsInPortal.size; ++visitorIndex)
        {
            Entity visitor = visitorsInPortal.get(visitorIndex);
            if (visitor.getId() == entity.getId())
            {
                captured = visitorIndex;
            }
        }

        if (triggerComponent.triggered && captured < 0)
        {
            System.out.println("Captured!");
            visitorsInPortal.add(entity);
        }
        else if (!triggerComponent.triggered && captured >= 0)
        {
            System.out.println("Released!");
            visitorsInPortal.removeIndex(captured);
        }
    }

    public boolean haveVisit(float time)
    {
        if (time > 15 && time < 20)
        {
            if (visitorsInPortal.size == numberOfVisitors)
            {
                return true;
            }
        }

        return false;
    }
}
