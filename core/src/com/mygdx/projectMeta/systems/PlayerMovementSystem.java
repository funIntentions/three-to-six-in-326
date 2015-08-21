package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.projectMeta.box2d.PlayerUserData;
import com.mygdx.projectMeta.components.InputComponent;
import com.mygdx.projectMeta.components.PhysicsComponent;
import com.mygdx.projectMeta.components.PlayerComponent;
import com.mygdx.projectMeta.components.TransformComponent;
import com.mygdx.projectMeta.utils.Constants;

public class PlayerMovementSystem extends IteratingSystem {

    public float desiredAngle;
    public float bodyAngle;

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<InputComponent> mm;
    private ComponentMapper<PhysicsComponent> pm;

    public PlayerMovementSystem() {
        super(Family.getFor(TransformComponent.class, InputComponent.class, PhysicsComponent.class, PlayerComponent.class));

        desiredAngle = 0;
        bodyAngle = 0;

        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(InputComponent.class);
        pm = ComponentMapper.getFor(PhysicsComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent positionComponent = tm.get(entity);
        InputComponent inputComponent = mm.get(entity);
        PhysicsComponent physicsComponent = pm.get(entity);

        Vector2 currentVelocity = physicsComponent.body.getLinearVelocity();
        float currentVelocitySqr = currentVelocity.len2();

        if (currentVelocitySqr > Constants.MAX_VELOCITY_SQR) {
            float factor = Constants.MAX_VELOCITY_SQR / currentVelocitySqr;
            currentVelocity.scl(factor);
            physicsComponent.body.setLinearVelocity(currentVelocity);
        }

        Vector2 movement = new Vector2();
        if ((inputComponent.movementInput & Constants.FORWARD) != 0) {
            movement.add(getFacingDirection());
        }

        if ((inputComponent.movementInput & Constants.BACKWARD) != 0) {
            movement.add(getFacingDirection().scl(-1));
        }

        if ((inputComponent.movementInput & Constants.LEFT) != 0) {
            movement.add(getRightDirection().scl(-1));
        }

        if ((inputComponent.movementInput & Constants.RIGHT) != 0) {
            movement.add(getRightDirection());
        }

        movement = movement.nor();
        movement.scl(((PlayerUserData) physicsComponent.body.getUserData()).getLinearForce());

        physicsComponent.body.applyLinearImpulse(movement, physicsComponent.body.getWorldCenter(), true);

        // face this
        {
            Vector2 toTarget = new Vector2(
                    inputComponent.faceThis.x - physicsComponent.body.getPosition().x,
                    inputComponent.faceThis.y - physicsComponent.body.getPosition().y);
            desiredAngle = (float) Math.atan2(-toTarget.x, toTarget.y);
        }

        bodyAngle = physicsComponent.body.getAngle();
        float nextAngle = bodyAngle + physicsComponent.body.getAngularVelocity() / 60.0f;
        float totalRotation = desiredAngle - nextAngle;
        while (totalRotation < -180 * MathUtils.degreesToRadians) totalRotation += 360 * MathUtils.degreesToRadians;
        while (totalRotation > 180 * MathUtils.degreesToRadians) totalRotation -= 360 * MathUtils.degreesToRadians;
        float desiredAngularVelocity = totalRotation * 60f;
        float torque = physicsComponent.body.getInertia() * desiredAngularVelocity / (1f / 60f);
        physicsComponent.body.applyTorque(torque, true);
        //float change = Constants.PLAYER_LEGS_ANGULAR_CHANGE * MathUtils.degreesToRadians;
        //float newAngle = bodyAngle + Math.min(change, Math.max(-change, totalRotation));
        //physicsComponent.body.setTransform(physicsComponent.body.getPosition(), newAngle);

        positionComponent.position.set(physicsComponent.body.getPosition().x, physicsComponent.body.getPosition().y, 0);
        positionComponent.rotation = physicsComponent.body.getAngle();
        positionComponent.desiredRotation = desiredAngle;
    }

    public Vector2 getFacingDirection() {
        Vector2 facing = new Vector2(0, 1).rotate(bodyAngle * (float) (180 / Math.PI));
        return facing;
    }


    public Vector2 getRightDirection() {
        Vector2 right = new Vector2(1, 0).rotate(bodyAngle * (float) (180 / Math.PI));
        return right;
    }
}
