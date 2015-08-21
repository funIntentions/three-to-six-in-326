package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.projectMeta.box2d.EntityUserData;
import com.mygdx.projectMeta.box2d.HandUserData;
import com.mygdx.projectMeta.box2d.UserData;
import com.mygdx.projectMeta.components.PhysicsComponent;
import com.mygdx.projectMeta.enums.UserDataType;

/**
 * Created by Dan on 8/17/2015.
 */
public class ContactSystem extends IteratingSystem implements ContactListener {
    private World world;
    private ComponentMapper<PhysicsComponent> physicsMapper;

    public ContactSystem(World world) {
        super(Family.getFor(PhysicsComponent.class));

        physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);

        this.world = world;
    }


    @Override
    public void processEntity(Entity entity, float deltaTime) {

    }


    public void beginContact(Contact contact) {
        UserData fixtureUserDataA = (UserData) contact.getFixtureA().getUserData();
        UserData fixtureUserDataB = (UserData) contact.getFixtureB().getUserData();

        if (fixtureUserDataA == null || fixtureUserDataB == null)
            return;

        if (fixtureUserDataA.getUserDataType() == UserDataType.HAND && fixtureUserDataB.getUserDataType() == UserDataType.ENTITY) {
            HandUserData handUserData = (HandUserData) fixtureUserDataA;
            EntityUserData entityUserData = (EntityUserData) fixtureUserDataB;

            handUserData.setEntityTouching(entityUserData.getEntity());
        } else if (fixtureUserDataB.getUserDataType() == UserDataType.HAND && fixtureUserDataA.getUserDataType() == UserDataType.ENTITY) {

            HandUserData handUserData = (HandUserData) fixtureUserDataB;
            EntityUserData entityUserData = (EntityUserData) fixtureUserDataA;

            handUserData.setEntityTouching(entityUserData.getEntity());
        }
    }

    public void endContact(Contact contact) {

        UserData fixtureUserDataA = (UserData) contact.getFixtureA().getUserData();
        UserData fixtureUserDataB = (UserData) contact.getFixtureB().getUserData();

        if (fixtureUserDataA == null || fixtureUserDataB == null)
            return;

        if (fixtureUserDataA.getUserDataType() == UserDataType.HAND && fixtureUserDataB.getUserDataType() == UserDataType.ENTITY) {
            HandUserData handUserData = (HandUserData) fixtureUserDataA;
            EntityUserData entityUserData = (EntityUserData) fixtureUserDataB;

            if (handUserData.getEntityTouching() != null && handUserData.getEntityTouching().getId() == entityUserData.getEntity().getId())
                handUserData.setEntityTouching(null);

        } else if (fixtureUserDataB.getUserDataType() == UserDataType.HAND && fixtureUserDataA.getUserDataType() == UserDataType.ENTITY) {

            HandUserData handUserData = (HandUserData) fixtureUserDataB;
            EntityUserData entityUserData = (EntityUserData) fixtureUserDataA;

            if (handUserData.getEntityTouching() != null && handUserData.getEntityTouching().getId() == entityUserData.getEntity().getId())
                handUserData.setEntityTouching(null);
        }
    }

    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
