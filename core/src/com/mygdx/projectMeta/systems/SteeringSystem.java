package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.projectMeta.Ray;
import com.mygdx.projectMeta.components.PhysicsComponent;
import com.mygdx.projectMeta.components.SteeringComponent;
import com.mygdx.projectMeta.components.TransformComponent;
import com.mygdx.projectMeta.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 7/25/2015.
 */
public class SteeringSystem extends IteratingSystem
{
    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<SteeringComponent> steeringMapper;
    private World world;

    public SteeringSystem(World world)
    {
        super(Family.getFor(TransformComponent.class, PhysicsComponent.class, SteeringComponent.class));

        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);
        steeringMapper= ComponentMapper.getFor(SteeringComponent.class);
        this.world = world;
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
        transformComponent.rotation = physicsComponent.body.getAngle();

        if (steeringComponent.seekOn)
        {
            PhysicsComponent targetPhysicsComponent = physicsMapper.get(steeringComponent.target);

            //SteeringOutput steeringOutput = seek(targetPhysicsComponent.body.getPosition(), physicsComponent.body.getPosition());
            SteeringOutput steeringOutput = avoidThings(world, physicsComponent, steeringComponent);
            physicsComponent.body.applyForce(steeringOutput.linear, physicsComponent.body.getWorldCenter(), true);

            Vector2 faceThis = new Vector2(physicsComponent.body.getPosition()).add(steeringOutput.linear);
            faceThis(faceThis, physicsComponent);


        }
    }

    class SteeringOutput
    {
        public Vector2 linear;
        public float angular;
    }

    public SteeringOutput seek(Vector2 target, Vector2 agentPos)
    {
        float force = 400;
        SteeringOutput steering = new SteeringOutput();

        Vector2 toTarget = target;
        toTarget.sub(agentPos);

        steering.linear = toTarget;

        steering.linear.nor();
        steering.linear.scl(force);

        steering.angular = 0;

        return steering;
    }

    public void faceThis(Vector2 faceThis, PhysicsComponent physicsComponent)
    {
        Vector2 toTarget = new Vector2(
                faceThis.x - physicsComponent.body.getPosition().x,
                faceThis.y - physicsComponent.body.getPosition().y);
        float desiredAngle = (float) Math.atan2(-toTarget.x, toTarget.y);

        float bodyAngle = physicsComponent.body.getAngle();
        float totalRotation = desiredAngle - bodyAngle;
        while ( totalRotation < -180 * MathUtils.degreesToRadians) totalRotation += 360 * MathUtils.degreesToRadians;
        while ( totalRotation >  180 * MathUtils.degreesToRadians) totalRotation -= 360 * MathUtils.degreesToRadians;
        float change = Constants.PLAYER_LEGS_ANGULAR_CHANGE * MathUtils.degreesToRadians; // TODO: Remove the player angular constant
        float newAngle = bodyAngle + Math.min(change, Math.max(-change, totalRotation));
        physicsComponent.body.setTransform(physicsComponent.body.getPosition(), newAngle); // TODO: Could I do this be applying a torque instead?
    }

    private void CreateFeelers(PhysicsComponent agentPhysics, SteeringComponent agentSteering)
    {
        //feeler pointing straight in front
        //Ray front = new Ray(agentPos.GetPosition(), agentkinematic.heading);
        //agentSteering.feelers[0].Position = agentPos.GetPosition();
        //agentSteering.feelers[0].Direction = agentkinematic.heading;

        ////feeler to left
        Vector2 heading = new Vector2(1,0).rotateRad(agentPhysics.body.getAngle());
        Vector2 leftFeelerDirection = new Vector2(heading);
        leftFeelerDirection.rotateRad((float) (Math.PI / 3f));

        Ray leftRay = new Ray();

        leftRay.position.set(agentPhysics.body.getPosition());
        leftRay.direction.set(leftFeelerDirection);
        leftRay.length = 4.0f;

        agentSteering.feelers.add(leftRay);

        ////feeler to right
        //temp = agentkinematic.heading;
        //temp = Util.Vec3RotateAroundOrigin(temp, (float)(Math.PI / 2) * 0.5f);
        //agentSteering.feelers.Add(agentPos.GetPosition() + (agentSteering.feelerLength / 2.0f * temp));
        Vector2 rightFeelerDirection =  new Vector2(heading);
        rightFeelerDirection.rotateRad(-(float)(Math.PI / 3f));

        Ray rightRay = new Ray();

        rightRay.position.set(agentPhysics.body.getPosition());
        rightRay.direction.set(rightFeelerDirection);
        rightRay.length = 4.0f;

        agentSteering.feelers.add(rightRay);
    }

    public class AvoidanceCallBack implements RayCastCallback
    {
        private Fixture fixture = null;
        private Vector2 point = null;
        private Vector2 normal = null;
        private float fraction = 0;

        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction)
        {
            this.fixture = fixture;
            this.point = point;
            this.normal = normal;
            this.fraction = fraction;
            return fraction;
        }
    }

    public SteeringOutput avoidThings(World world, PhysicsComponent agentPhysics, SteeringComponent agentSteering)
    {
        float force = 400;
        SteeringOutput steering = new SteeringOutput();

        CreateFeelers(agentPhysics, agentSteering);

        for (Ray whiskerRay : agentSteering.feelers)
        {
            AvoidanceCallBack avoidanceCallBack = new AvoidanceCallBack();

            world.rayCast(avoidanceCallBack, whiskerRay.position, new Vector2(whiskerRay.position).add(new Vector2(whiskerRay.direction).scl(whiskerRay.length)));

            if (avoidanceCallBack.fixture != null)
            {
                steering.linear.add(avoidanceCallBack.normal.scl(force));
            }
        }

        return steering;
    }
}
