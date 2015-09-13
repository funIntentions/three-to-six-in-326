package com.mygdx.projectMeta;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
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
        createToilet();
        createBathtub(player, actionText);
        //createDemon(player);
        createDucky();

        createCamera(player);
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

    private Entity createToilet() {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        FurnitureComponent furnitureComponent = new FurnitureComponent();

        physicsComponent.body = WorldUtils.createStaticFurniture(world, Constants.TOILET_X, Constants.TOILET_Y, Constants.TOILET_WIDTH, Constants.TOILET_HEIGHT);
        physicsComponent.body.setUserData(new EntityUserData(entity));
        textureComponent.textureRegion = Assets.toilet;

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

    public Entity createPortal()
    {
        Entity entity = new Entity();

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position.set(new Vector3(Constants.TV_X, Constants.TV_Y, 0));

        entity.add(transformComponent);

        engine.addEntity(entity);

        return entity;
    }

    public Entity createThreeOClockVisitor(Entity portal) {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        ThreeOClockVisitorComponent threeOClockVisitorComponent = new ThreeOClockVisitorComponent();
        TriggerComponent triggerComponent = new TriggerComponent();
        AnimationComponent animationComponent = new AnimationComponent();
        StateComponent stateComponent = new StateComponent();
        SteeringComponent steeringComponent = new SteeringComponent();

        steeringComponent.wanderOn = true;
        steeringComponent.force = 200f;
        stateComponent.set(ThreeOClockVisitorComponent.MOVING);
        animationComponent.animations.put(ThreeOClockVisitorComponent.IDLE, Assets.antIdle);
        animationComponent.animations.put(ThreeOClockVisitorComponent.MOVING, Assets.antMoving);
        physicsComponent.body = WorldUtils.createDynamicOvalBody(world, 10, 10, Constants.ANT_DEMON_WIDTH, Constants.ANT_DEMON_HEIGHT, Constants.DEMON_DAMPING, Constants.DEMON_ANGULAR_DAMPING * 100, Constants.DEMON_DENSITY, entity);
        physicsComponent.body.setUserData(new EntityUserData(entity));
        transformComponent.position.set(physicsComponent.body.getPosition().x, physicsComponent.body.getPosition().y, 0.0f);
        triggerComponent.triggerer = portal;
        triggerComponent.range = 4;

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(threeOClockVisitorComponent);
        entity.add(triggerComponent);
        entity.add(stateComponent);
        entity.add(animationComponent);
        entity.add(steeringComponent);

        engine.addEntity(entity);

        return entity;
    }
}
