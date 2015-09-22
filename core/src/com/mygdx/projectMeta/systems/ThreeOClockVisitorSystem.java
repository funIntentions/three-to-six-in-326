package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
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
    private final int numberOfVisitors = 4;
    private int visitorCount = 0;
    private GameWorld gameWorld;
    private Entity portal;

    public ThreeOClockVisitorSystem (GameWorld gameWorld) {
        super(Family.getFor(ThreeOClockVisitorComponent.class, TriggerComponent.class));

        triggerMapper = ComponentMapper.getFor(TriggerComponent.class);

        this.gameWorld = gameWorld;

        portal = gameWorld.createPortal();
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
            System.out.println(entity.getId() + " Captured!");
            visitorsInPortal.add(entity);
        }
        else if (!triggerComponent.triggered && captured >= 0)
        {
            System.out.println(entity.getId() + " Released!");
            visitorsInPortal.removeIndex(captured);
        }
    }

    public boolean haveVisit(float time)
    {
        if (visitorsInPortal.size == numberOfVisitors) {
            return true;
        } else if (visitorCount < 1 && time > 2) {
            ++visitorCount;
            gameWorld.createThreeOClockVisitor(portal, new Vector2(20, 20));
        }/* else if (visitorCount < 2 && time > 3) {
            ++visitorCount;
            gameWorld.createThreeOClockVisitor(portal , new Vector2(10, 10));
        } else if (visitorCount < 3 && time > 4) {
            ++visitorCount;
            gameWorld.createThreeOClockVisitor(portal, new Vector2(20, 5));
        } else if (visitorCount < 4 && time > 5) {
            ++visitorCount;
            gameWorld.createThreeOClockVisitor(portal, new Vector2(5, 24));
        }*/

        return false;
    }
}
