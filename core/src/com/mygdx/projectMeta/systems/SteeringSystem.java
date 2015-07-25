package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.projectMeta.components.PhysicsComponent;
import com.mygdx.projectMeta.components.SteeringComponent;
import com.mygdx.projectMeta.components.TransformComponent;

/**
 * Created by Dan on 7/25/2015.
 */
public class SteeringSystem extends IteratingSystem
{
    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<SteeringComponent> steeringMapper;

    public SteeringSystem()
    {
        super(Family.getFor(TransformComponent.class, PhysicsComponent.class, SteeringComponent.class));

        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);
        steeringMapper= ComponentMapper.getFor(SteeringComponent.class);
    }

    public void processEntity(Entity entity, float deltaTime)
    {
        PhysicsComponent physicsComponent = physicsMapper.get(entity);
        TransformComponent transformComponent = transformMapper.get(entity);
        SteeringComponent steeringComponent = steeringMapper.get(entity);

        transformComponent.position.set(
                physicsComponent.body.getPosition().x,
                physicsComponent.body.getPosition().y,
                transformComponent.position.z);

        if (steeringComponent.seekOn)
        {
            PhysicsComponent targetPhysicsComponent = physicsMapper.get(steeringComponent.target);

            SteeringOutput steeringOutput = seek(targetPhysicsComponent.body.getPosition(), physicsComponent.body.getPosition());
            physicsComponent.body.applyForce(steeringOutput.linear, physicsComponent.body.getWorldCenter(), true);
        }
    }

    class SteeringOutput
    {
        public Vector2 linear;
        public float angular;
    }

    public SteeringOutput seek(Vector2 target, Vector2 agentPos)
    {
        float force = 125;
        SteeringOutput steering = new SteeringOutput();

        Vector2 toTarget = target;
        toTarget.sub(agentPos);

        steering.linear = toTarget;

        steering.linear.nor();
        steering.linear.scl(force);

        steering.angular = 0;

        return steering;
    }
}
