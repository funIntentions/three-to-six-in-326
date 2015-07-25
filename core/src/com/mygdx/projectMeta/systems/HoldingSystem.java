package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.mygdx.projectMeta.components.HoldableComponent;
import com.mygdx.projectMeta.components.InputComponent;
import com.mygdx.projectMeta.components.PhysicsComponent;
import com.mygdx.projectMeta.components.TriggerComponent;

/**
 * Created by Dan on 7/19/2015.
 */
public class HoldingSystem extends IteratingSystem
{
    private World world;
    private ComponentMapper<HoldableComponent> holdableMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private ComponentMapper<TriggerComponent> triggerMapper;
    private ComponentMapper<InputComponent> inputMapper;
    private float throwingStrength = 5;

    public HoldingSystem(World world) {
        super(Family.getFor(PhysicsComponent.class, HoldableComponent.class, TriggerComponent.class, InputComponent.class));

        holdableMapper = ComponentMapper.getFor(HoldableComponent.class);
        physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);
        triggerMapper = ComponentMapper.getFor(TriggerComponent.class);
        inputMapper = ComponentMapper.getFor(InputComponent.class);

        this.world = world;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        PhysicsComponent physicsComponent = physicsMapper.get(entity);
        HoldableComponent holdableComponent = holdableMapper.get(entity);
        TriggerComponent triggerComponent = triggerMapper.get(entity);
        InputComponent inputComponent = inputMapper.get(entity);

        if (holdableComponent.holder != null
                && triggerComponent.triggered
                && Gdx.input.isKeyJustPressed(Input.Keys.E)) // TODO: refactor Input Component... It's not performing very well
        {
            PhysicsComponent holdersPhysicsComponent = physicsMapper.get(triggerComponent.triggerer);

            if (!holdableComponent.held)
            {

                PrismaticJointDef jointDef = new PrismaticJointDef();

                jointDef.bodyA = holdersPhysicsComponent.body;
                jointDef.bodyB = physicsComponent.body;
                jointDef.localAnchorA.set(0.6f,0.8f);
                jointDef.localAnchorB.set(0,0);
                jointDef.localAxisA.set(1,0);
                jointDef.referenceAngle = 0;

                jointDef.referenceAngle = 0;
                jointDef.lowerTranslation = 0.0f;
                jointDef.upperTranslation = 0.0f;
                jointDef.enableLimit = true;
                jointDef.maxMotorForce = 1.0f;
                jointDef.motorSpeed = 0.25f;
                jointDef.enableMotor = true;

                holdableComponent.distanceJoint = world.createJoint(jointDef);
                holdableComponent.held = true;
            }
            else // Drop
            {
                world.destroyJoint(holdableComponent.distanceJoint);
                holdableComponent.distanceJoint = null;
                holdableComponent.held = false;
            }
        }

        if (holdableComponent.holder != null
                && holdableComponent.held
                && Gdx.input.isKeyJustPressed(Input.Keys.F)) // Throw
        {
            world.destroyJoint(holdableComponent.distanceJoint);
            holdableComponent.distanceJoint = null;
            holdableComponent.held = false;

            PhysicsComponent holdersPhysicsComponent = physicsMapper.get(triggerComponent.triggerer);
            Vector2 force = new Vector2(0,1).rotateRad(holdersPhysicsComponent.body.getAngle());
            force.scl(throwingStrength);

            physicsComponent.body.applyLinearImpulse(force, physicsComponent.body.getWorldCenter(), true);
        }
    }
}
