package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.mygdx.projectMeta.GameWorld;
import com.mygdx.projectMeta.components.*;

/**
 * Created by Dan on 8/20/2015.
 */
public class ThreeOClockSystem extends IteratingSystem {
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<AnimationComponent> animationMapper;
    private ComponentMapper<SteeringComponent> steeringMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<ThreeOClockVisitorComponent> visitorMapper;
    private Array<Entity> visitorsInPortal = new Array<Entity>();
    private int visitorCount = 0;
    private GameWorld gameWorld;
    private Entity portal = null;
    private float portalStrength = 0.0f;
    private float portalRange = 3;
    private float portalBlastRadius = 15.0f;
    private float portalBlastStrength = 200.0f;
    private boolean finished = false;

    public ThreeOClockSystem (GameWorld gameWorld) {
        super(Family.getFor(PhysicsComponent.class));

        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        steeringMapper = ComponentMapper.getFor(SteeringComponent.class);
        physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        visitorMapper = ComponentMapper.getFor(ThreeOClockVisitorComponent.class);

        this.gameWorld = gameWorld;
    }

    private void applyPortalForce(Body body, Vector2 portalPosition) {
        Vector2 toPortal = new Vector2(portalPosition.x - body.getPosition().x,
                                        portalPosition.y - body.getPosition().y);
        float distance = toPortal.len();
        toPortal.nor();
        toPortal.set(toPortal.x * portalStrength/distance, toPortal.y * portalStrength/distance);
        toPortal.scl(toPortal.len());

        if (toPortal.len() > portalStrength) {
            toPortal.nor();
            toPortal.scl(portalStrength);
        }

        body.applyForce(toPortal, body.getWorldCenter(), true);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

        PhysicsComponent physicsComponent = physicsMapper.get(entity);

        if (portal != null)
        {
            TransformComponent portalTransform = transformMapper.get(portal);
            applyPortalForce(physicsComponent.body, new Vector2(portalTransform.position.x, portalTransform.position.y));

            if (visitorMapper.has(entity)) {
                boolean captured = visitorsInPortal.contains(entity, true);
                boolean inRange = Vector3.dst(portalTransform.position.x, portalTransform.position.y, portalTransform.position.z, physicsComponent.body.getPosition().x, physicsComponent.body.getPosition().y, 0) < portalRange;

                if (inRange && !captured)
                {
                    System.out.println(entity.getId() + " Captured!");
                    visitorsInPortal.add(entity);
                }
                else if (!inRange && captured)
                {
                    System.out.println(entity.getId() + " Released!");
                    visitorsInPortal.removeValue(entity, true);
                }
            }
        }
    }

    public boolean timeToVisit(float time) {
        return (time > 4 && !finished);
    }

    public void haveVisit(float time)
    {
        if (visitorCount < 1 && time > 4) {
            ++visitorCount;
            gameWorld.createThreeOClockVisitor(new Vector2(20, 20));
        } else if (visitorCount < 2 && time > 5) {
            ++visitorCount;
            gameWorld.createThreeOClockVisitor(new Vector2(10, 10));
        } else if (visitorCount < 3 && time > 6) {
            ++visitorCount;
            gameWorld.createThreeOClockVisitor(new Vector2(20, 5));
        } else if (visitorCount < 4 && time > 7) {
            ++visitorCount;
            gameWorld.createThreeOClockVisitor(new Vector2(5, 24));
        } else if (time > 15 && portal == null) {
            portal = gameWorld.createPortal();
        } else if (portal != null) {
            portalStrength = time * 6; // increase portal strength with time
        }

        if ( time > 30 && visitorsInPortal.size == visitorCount) {
            for (Entity visitor : visitorsInPortal) {
                gameWorld.removeEntity(visitor); // TODO: If the player is holding onto the visitor as it is destroyed the game will crash next time the player tries to pick something up.
            }
            visitorsInPortal.clear();
            TransformComponent portalTransform = transformMapper.get(portal);
            gameWorld.createBlastImpulseRadius(new Vector2(portalTransform.position.x, portalTransform.position.y), portalBlastRadius, portalBlastStrength);
            gameWorld.removeEntity(portal);
            portal = null;
            finished = true;
        }
    }
}
