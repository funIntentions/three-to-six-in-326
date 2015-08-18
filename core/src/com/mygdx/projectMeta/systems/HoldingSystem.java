package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.projectMeta.box2d.HandUserData;
import com.mygdx.projectMeta.box2d.UserData;
import com.mygdx.projectMeta.components.*;
import com.mygdx.projectMeta.enums.UserDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 7/19/2015.
 */
public class HoldingSystem extends IteratingSystem
{
    private World world;
    private ComponentMapper<HoldableComponent> holdableMapper;
    private ComponentMapper<PhysicsComponent> physicsMapper;
    private float throwingStrength = 5;

    public HoldingSystem(World world) {
        super(Family.getFor(PhysicsComponent.class, PlayerComponent.class, InputComponent.class));

        holdableMapper = ComponentMapper.getFor(HoldableComponent.class);
        physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);

        this.world = world;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        PhysicsComponent holdersPhysicsComponent = physicsMapper.get(entity);
        Array<Fixture> fixtures = holdersPhysicsComponent.body.getFixtureList();

        Entity touching = null;

        for (Fixture fixture : fixtures)
        {
            if (fixture.getUserData() != null && ((UserData)fixture.getUserData()).getUserDataType() == UserDataType.HAND)
            {
                touching = ((HandUserData)fixture.getUserData()).getEntityTouching();
            }
        }

        if (touching == null || !holdableMapper.has(touching) || !physicsMapper.has(touching))
            return;

        HoldableComponent holdableComponent = holdableMapper.get(touching);
        PhysicsComponent physicsComponent = physicsMapper.get(touching);

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) // TODO: refactor Input Component... It's not performing very well
        {
            if (!holdableComponent.held)
            {

                PrismaticJointDef jointDef = new PrismaticJointDef();

                jointDef.initialize(holdersPhysicsComponent.body, physicsComponent.body, holdersPhysicsComponent.body.getWorldCenter(), new Vector2(1,0));

                jointDef.lowerTranslation = 0f;

                jointDef.upperTranslation = 0f;

                jointDef.enableLimit = true;

                jointDef.maxMotorForce = 1.0f;

                jointDef.motorSpeed = 0.0f;

                jointDef.enableMotor = true;

//                jointDef.bodyA = holdersPhysicsComponent.body;
//                jointDef.bodyB = physicsComponent.body;
//                jointDef.localAnchorA.set(0.6f,0.8f);
//                jointDef.localAnchorB.set(0,0);
//                jointDef.localAxisA.set(1,0);
//                jointDef.referenceAngle = 0;
//
//                jointDef.referenceAngle = 0;
//                jointDef.lowerTranslation = 0.0f;
//                jointDef.upperTranslation = 0.0f;
//                jointDef.enableLimit = true;
//                jointDef.maxMotorForce = 1.0f;
//                jointDef.motorSpeed = 0.25f;
//                jointDef.enableMotor = true;
//
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

        if (holdableComponent.held
                && Gdx.input.isKeyJustPressed(Input.Keys.F)) // Throw
        {
            world.destroyJoint(holdableComponent.distanceJoint);
            holdableComponent.distanceJoint = null;
            holdableComponent.held = false;

            Vector2 force = new Vector2(0,1).rotateRad(holdersPhysicsComponent.body.getAngle());
            force.scl(throwingStrength);

            physicsComponent.body.applyLinearImpulse(force, physicsComponent.body.getWorldCenter(), true);
        }
    }
}
