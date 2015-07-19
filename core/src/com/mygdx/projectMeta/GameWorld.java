package com.mygdx.projectMeta;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.projectMeta.box2d.RunnerUserData;
import com.mygdx.projectMeta.components.*;
import com.mygdx.projectMeta.systems.RenderingSystem;
import com.mygdx.projectMeta.utils.Constants;
import com.mygdx.projectMeta.utils.WorldUtils;

/**
 * Created by Dan on 7/18/2015.
 */
public class GameWorld
{
    Engine engine;
    World world;

    public GameWorld(Engine engine, World world)
    {
        this.engine = engine;
        this.world = world;
    }

    public void createWorld()
    {
        Entity player = createPlayer();
        createPlayerTorso(player);
        createCouch();
        createTV();
        createToilet();
        createBathtub(player);

        createCamera(player);
    }

    private Entity createPlayer()
    {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        InputComponent inputComponent = new InputComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        AnimationComponent animationComponent = new AnimationComponent();
        StateComponent stateComponent = new StateComponent();
        PlayerComponent playerComponent = new PlayerComponent();

        physicsComponent.body = WorldUtils.createPlayer(world);
        physicsComponent.userData = new RunnerUserData();

        stateComponent.set(PlayerComponent.STATE_STILL);
        animationComponent.animations.put(PlayerComponent.STATE_WALKING, Assets.playerWalking);
        animationComponent.animations.put(PlayerComponent.STATE_STILL, Assets.playerStill);

        entity.add(textureComponent);
        entity.add(transformComponent);
        entity.add(inputComponent);
        entity.add(physicsComponent);
        entity.add(animationComponent);
        entity.add(stateComponent);
        entity.add(playerComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createPlayerTorso(Entity player)
    {
        Entity entity = new Entity();

        TorsoComponent torsoComponent = new TorsoComponent();
        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();

        torsoComponent.target = player;
        textureComponent.textureRegion = Assets.playerTorso;

        entity.add(torsoComponent);
        entity.add(textureComponent);
        entity.add(transformComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createCouch()
    {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        FurnitureComponent furnitureComponent = new FurnitureComponent();

        physicsComponent.body = WorldUtils.createCouch(world);
        textureComponent.textureRegion = Assets.couch;

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(furnitureComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createTV()
    {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        FurnitureComponent furnitureComponent = new FurnitureComponent();

        physicsComponent.body = WorldUtils.createTV(world);
        textureComponent.textureRegion = Assets.tv;

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(furnitureComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createToilet()
    {
        Entity entity = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        TransformComponent transformComponent = new TransformComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        FurnitureComponent furnitureComponent = new FurnitureComponent();

        physicsComponent.body = WorldUtils.createStaticFurniture(world, Constants.TOILET_X, Constants.TOILET_Y, Constants.TOILET_WIDTH, Constants.TOILET_HEIGHT);
        textureComponent.textureRegion = Assets.toilet;

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(furnitureComponent);

        engine.addEntity(entity);

        return entity;
    }

    private Entity createBathtub(Entity triggerEntity)
    {
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

        triggerComponent.range = 4;
        triggerComponent.triggerer = triggerEntity;
        physicsComponent.body = WorldUtils.createStaticFurniture(world, Constants.BATHTUB_X, Constants.BATHTUB_Y, Constants.BATHTUB_WIDTH, Constants.BATHTUB_HEIGHT);
        stateComponent.set(BathtubComponent.STATE_DRAINED);
        animationComponent.animations.put(BathtubComponent.STATE_RUNNING, Assets.bathtubRunning);
        animationComponent.animations.put(BathtubComponent.STATE_DRAINING, Assets.bathtubDraining);
        animationComponent.animations.put(BathtubComponent.STATE_DRAINED, Assets.bathtubDrained);
        animationComponent.animations.put(BathtubComponent.STATE_RAN, Assets.bathtubRan);

        entity.add(transformComponent);
        entity.add(physicsComponent);
        entity.add(textureComponent);
        entity.add(furnitureComponent);
        entity.add(bathtubComponent);
        entity.add(animationComponent);
        entity.add(stateComponent);
        entity.add(triggerComponent);
        entity.add(inputComponent);

        engine.addEntity(entity);

        return entity;
    }

    private void createCamera(Entity target)
    {
        Entity entity = new Entity();

        CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.camera = engine.getSystem(RenderingSystem.class).getCamera();
        cameraComponent.target = target;

        entity.add(cameraComponent);

        engine.addEntity(entity);
    }

}
