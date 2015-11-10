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
public class ThreeOClockVisitorSystem extends IteratingSystem {
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<AnimationComponent> animationMapper;
    private ComponentMapper<SteeringComponent> steeringMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<TransformComponent> transformMapper;
    private Array<Entity> visitorsInPortal = new Array<Entity>();
    private int visitorCount = 0;
    private GameWorld gameWorld;
    private Entity portal = null;
    private float portalStrength = 0.0f;
    private float portalRange = 2;
    private boolean finished = false;

    public ThreeOClockVisitorSystem (GameWorld gameWorld) {
        super(Family.getFor(ThreeOClockVisitorComponent.class, PhysicsComponent.class));

        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        steeringMapper = ComponentMapper.getFor(SteeringComponent.class);
        physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);

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

        System.out.println(toPortal.len());
        body.applyForce(toPortal, body.getWorldCenter(), true);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

        AnimationComponent animationComponent = animationMapper.get(entity);
        StateComponent stateComponent = stateMapper.get(entity);
        SteeringComponent steeringComponent = steeringMapper.get(entity);
        PhysicsComponent physicsComponent = physicsMapper.get(entity);

        Animation animation = animationComponent.animations.get(stateComponent.get());

        if (animation.getAnimationDuration() <= stateComponent.time) {
            if (stateComponent.get() == ThreeOClockVisitorComponent.SPAWNING) {
                steeringComponent.wanderOn = true;
                stateComponent.set(ThreeOClockVisitorComponent.MOVING);
            }
        }

        if (portal != null)
        {
            TransformComponent portalTransform = transformMapper.get(portal);
            applyPortalForce(physicsComponent.body, new Vector2(portalTransform.position.x, portalTransform.position.y));

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
                gameWorld.removeEntity(visitor);
            }
            visitorsInPortal.clear();
            gameWorld.removeEntity(portal);
            portal = null;
            finished = true;
        }
    }
}
