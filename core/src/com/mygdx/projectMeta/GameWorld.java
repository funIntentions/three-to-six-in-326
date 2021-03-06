package com.mygdx.projectMeta;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.projectMeta.box2d.BodyQueryCallback;
import com.mygdx.projectMeta.box2d.EntityUserData;
import com.mygdx.projectMeta.box2d.PlayerUserData;
import com.mygdx.projectMeta.components.*;
import com.mygdx.projectMeta.systems.RenderingSystem;
import com.mygdx.projectMeta.systems.ThreeOClockVisitorSystem;
import com.mygdx.projectMeta.utils.Constants;
import com.mygdx.projectMeta.utils.WorldUtils;

/**
 * Created by Dan on 7/18/2015.
 */
public class GameWorld {
    Engine engine;
    World world;
    private ComponentMapper<PhysicsComponent> physicsMapper;

    public GameWorld(Engine engine, World world) {
        this.engine = engine;
        this.world = world;
    }

    public void createWorld() {
        Entity actionText = createActionTextEntity();
        Entity player = createPlayer();
        createPlayerTorso(player);
        //createCouch();
        //createTV();
        createToilet(player);
        createBathtub(player, actionText);
        createDemon(player);
        createDucky();

        createCamera(player);

        physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);
    }

    private Entity createPlayer() {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        InputComponent inputComponent = new InputComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        AnimationComponent animationComponent = new AnimationComponent();
        StateComponent stateComponent = new StateComponent();
        PlayerComponent playerComponent = new PlayerComponent();
        SoundComponent soundComponent = new SoundComponent();
        HandComponent handComponent = new HandComponent();

        physicsComponent.body = WorldUtils.createPlayer(world, entity);
        physicsComponent.body.setUserData(new PlayerUserData());

        stateComponent.set(PlayerComponent.STATE_STILL);
        animationComponent.animations.put(PlayerComponent.STATE_WALKING, Assets.playerLegsWalking);
        animationComponent.animations.put(PlayerComponent.STATE_STILL, Assets.playerLegsIdle);

        soundComponent.sound.add(Assets.slipperStepsSound);

        entity.add(textureComponent);
        entity.add(transformComponent);
        entity.add(inputComponent);
        entity.add(physicsComponent);
        entity.add(animationComponent);
        entity.add(stateComponent);
        entity.add(playerComponent);
        entity.add(soundComponent);
        entity.add(handComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createPlayerTorso(Entity player) {
        Entity entity = new Entity();

        TorsoComponent torsoComponent = new TorsoComponent();
        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        StateComponent stateComponent = new StateComponent();
        AnimationComponent animationComponent = new AnimationComponent();

        torsoComponent.target = player;
        transformComponent.position.set(Constants.PLAYER_X, Constants.PLAYER_Y, -1);
        stateComponent.set(PlayerComponent.STATE_STILL);
        animationComponent.animations.put(PlayerComponent.STATE_WALKING, Assets.playerTorsoWalking);
        animationComponent.animations.put(PlayerComponent.STATE_STILL, Assets.playerTorsoIdle);
        animationComponent.animations.put(PlayerComponent.STATE_GRAB, Assets.playerGrabbing);

        entity.add(torsoComponent);
        entity.add(textureComponent);
        entity.add(transformComponent);
        entity.add(animationComponent);
        entity.add(stateComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createCouch() {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        FurnitureComponent furnitureComponent = new FurnitureComponent();

        physicsComponent.body = WorldUtils.createCouch(world, entity);
        physicsComponent.body.setUserData(new EntityUserData(entity));

        textureComponent.textureRegion = Assets.couch;

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(furnitureComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createTV() {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        FurnitureComponent furnitureComponent = new FurnitureComponent();
        StateComponent stateComponent = new StateComponent();
        AnimationComponent animationComponent = new AnimationComponent();

        physicsComponent.body = WorldUtils.createTV(world, entity);
        physicsComponent.body.setUserData(new EntityUserData(entity));

        stateComponent.set(TvComponent.STATE_STATIC_CHANNEL);
        animationComponent.animations.put(TvComponent.STATE_STATIC_CHANNEL, Assets.tvChannelStatic);

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(furnitureComponent);
        entity.add(stateComponent);
        entity.add(animationComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createDemon(Entity target) {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        SteeringComponent steeringComponent = new SteeringComponent();

        physicsComponent.body = WorldUtils.createDemon(world, entity);
        physicsComponent.body.setUserData(new EntityUserData(entity));
        textureComponent.textureRegion = Assets.demonV1;
        transformComponent.position.set(physicsComponent.body.getPosition().x, physicsComponent.body.getPosition().y, 0.0f);
        steeringComponent.target = target;
        steeringComponent.seekOn = true;

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(steeringComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createToilet(Entity triggerEntity) {
        Entity entity = new Entity();

        ToiletComponent toiletComponent = new ToiletComponent();
        AnimationComponent animationComponent = new AnimationComponent();
        StateComponent stateComponent = new StateComponent();
        TriggerComponent triggerComponent = new TriggerComponent();
        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        FurnitureComponent furnitureComponent = new FurnitureComponent();
        InputComponent inputComponent = new InputComponent();

        stateComponent.set(ToiletComponent.STATE_IDLE);
        triggerComponent.range = 3;
        triggerComponent.triggerer = triggerEntity;
        physicsComponent.body = WorldUtils.createStaticFurniture(world, Constants.TOILET_X, Constants.TOILET_Y, Constants.TOILET_WIDTH, Constants.TOILET_HEIGHT);
        physicsComponent.body.setUserData(new EntityUserData(entity));
        animationComponent.animations.put(ToiletComponent.STATE_IDLE, Assets.toiletIdle);
        animationComponent.animations.put(ToiletComponent.STATE_FLUSHING, Assets.toiletFlush);

        entity.add(inputComponent);
        entity.add(toiletComponent);
        entity.add(stateComponent);
        entity.add(animationComponent);
        entity.add(triggerComponent);
        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(furnitureComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createBathtub(Entity triggerEntity, Entity actionText) {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        FurnitureComponent furnitureComponent = new FurnitureComponent();
        BathtubComponent bathtubComponent = new BathtubComponent();
        AnimationComponent animationComponent = new AnimationComponent();
        StateComponent stateComponent = new StateComponent();
        TriggerComponent triggerComponent = new TriggerComponent();
        InputComponent inputComponent = new InputComponent();
        SoundComponent soundComponent = new SoundComponent();

        triggerComponent.range = 4;
        triggerComponent.triggerer = triggerEntity;
        triggerComponent.actionTextEntity = actionText;
        physicsComponent.body = WorldUtils.createStaticFurniture(world, Constants.BATHTUB_X, Constants.BATHTUB_Y, Constants.BATHTUB_WIDTH, Constants.BATHTUB_HEIGHT);
        physicsComponent.body.setUserData(new EntityUserData(entity));
        stateComponent.set(BathtubComponent.STATE_DRAINED);
        animationComponent.animations.put(BathtubComponent.STATE_RUNNING, Assets.bathtubRunning);
        animationComponent.animations.put(BathtubComponent.STATE_DRAINING, Assets.bathtubDraining);
        animationComponent.animations.put(BathtubComponent.STATE_DRAINED, Assets.bathtubDrained);
        animationComponent.animations.put(BathtubComponent.STATE_RAN, Assets.bathtubRan);

        soundComponent.sound.add(Assets.bathtubDrainingSound);
        soundComponent.sound.add(Assets.bathtubRunningSound);

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(furnitureComponent);
        entity.add(bathtubComponent);
        entity.add(animationComponent);
        entity.add(stateComponent);
        entity.add(triggerComponent);
        entity.add(inputComponent);
        entity.add(soundComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createDucky() {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        FurnitureComponent furnitureComponent = new FurnitureComponent();
        TriggerComponent triggerComponent = new TriggerComponent();
        HoldableComponent holdableComponent = new HoldableComponent();

        physicsComponent.body = WorldUtils.createDucky(world, entity);
        textureComponent.textureRegion = Assets.ducky;
        triggerComponent.range = 2f;

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(furnitureComponent);
        entity.add(holdableComponent);

        engine.addEntity(entity);

        return entity;
    }

    public Entity createActionTextEntity() {
        Entity entity = new Entity();

        TransformComponent transformComponent = new TransformComponent();
        TextComponent textComponent = new TextComponent();
        StateComponent stateComponent = new StateComponent();

        stateComponent.set(TextComponent.STATE_HIDDEN);
        transformComponent.position.set(50, 50, 0);
        textComponent.text = "Press E";
        textComponent.font = Assets.journalFont;
        textComponent.displayTime = 3;

        entity.add(stateComponent);
        entity.add(transformComponent);
        entity.add(textComponent);

        engine.addEntity(entity);

        return entity;
    }

    private void createCamera(Entity target) {
        Entity entity = new Entity();

        CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.camera = engine.getSystem(RenderingSystem.class).getCamera();
        cameraComponent.target = target;

        entity.add(cameraComponent);

        engine.addEntity(entity);
    }

    public void removeEntity(Entity entity) {
        if (physicsMapper.has(entity)) {
            PhysicsComponent physicsComponent = physicsMapper.get(entity);
            world.destroyBody(physicsComponent.body);
        }
        engine.removeEntity(entity);
    }

    public Entity createPortal()
    {
        Entity entity = new Entity();

        PortalComponent portalComponent = new PortalComponent();
        TransformComponent transformComponent = new TransformComponent();
        TextureComponent textureComponent = new TextureComponent();

        textureComponent.textureRegion = Assets.portal;
        transformComponent.position.set(new Vector3(Constants.TV_X, Constants.TV_Y, 0));

        entity.add(portalComponent);
        entity.add(transformComponent);
        entity.add(textureComponent);

        engine.addEntity(entity);

        return entity;
    }

    public Entity createThreeOClockVisitor(Vector2 position) {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        ThreeOClockVisitorComponent threeOClockVisitorComponent = new ThreeOClockVisitorComponent();
        AnimationComponent animationComponent = new AnimationComponent();
        StateComponent stateComponent = new StateComponent();
        SteeringComponent steeringComponent = new SteeringComponent();

        steeringComponent.force = 250f;
        stateComponent.set(ThreeOClockVisitorComponent.SPAWNING);
        animationComponent.animations.put(ThreeOClockVisitorComponent.SPAWNING, Assets.zap);
        animationComponent.animations.put(ThreeOClockVisitorComponent.IDLE, Assets.antIdle);
        animationComponent.animations.put(ThreeOClockVisitorComponent.MOVING, Assets.antMoving);
        physicsComponent.body = WorldUtils.createDynamicOvalBody(world, position.x, position.y, Constants.ANT_DEMON_WIDTH, Constants.ANT_DEMON_HEIGHT, Constants.DEMON_DAMPING, Constants.DEMON_ANGULAR_DAMPING, Constants.DEMON_DENSITY, entity);
        physicsComponent.body.setUserData(new EntityUserData(entity));
        transformComponent.position.set(physicsComponent.body.getPosition().x, physicsComponent.body.getPosition().y, 0.0f);

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(threeOClockVisitorComponent);
        entity.add(stateComponent);
        entity.add(animationComponent);
        entity.add(steeringComponent);

        engine.addEntity(entity);

        return entity;
    }

    public void createBlastImpulseRadius(Vector2 center, float blastRadius, float blastPower) {
        //find all bodies with fixtures in blast radius AABB
        BodyQueryCallback queryCallback = new BodyQueryCallback();

        Vector2 lowerBound = new Vector2(center).sub(new Vector2( blastRadius, blastRadius ));
        Vector2 upperBound = new Vector2(center).add(new Vector2( blastRadius, blastRadius ));
        world.QueryAABB(queryCallback, lowerBound.x, lowerBound.y, upperBound.x, upperBound.y);

        //check which of these bodies have their center of mass within the blast radius
        for (int i = 0; i < queryCallback.foundBodies.size; i++) {
            Body body = queryCallback.foundBodies.get(i);
            Vector2 bodyCenter = body.getWorldCenter();

            //ignore bodies outside the blast range
            if ( (new Vector2(bodyCenter).sub(center)).len() >= blastRadius )
                continue;

            applyBlastImpulse(body, center, bodyCenter, blastPower );
        }
    }

    private void applyBlastImpulse(Body body, Vector2 blastCenter, Vector2 applyPoint, float blastPower) {
        Vector2 blastDir = new Vector2(applyPoint).sub(blastCenter);
        float distance = blastDir.len();
        blastDir.nor();
        //ignore bodies exactly at the blast point - blast direction is undefined
        if ( distance == 0 )
            return;
        float invDistance = 1 / distance;
        float impulseMag = blastPower * invDistance * invDistance;
        blastDir.scl(impulseMag);
        body.applyLinearImpulse(blastDir, applyPoint, true);
    }
}
