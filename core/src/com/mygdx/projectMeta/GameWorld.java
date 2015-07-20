package com.mygdx.projectMeta;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.projectMeta.box2d.RunnerUserData;
import com.mygdx.projectMeta.components.*;
import com.mygdx.projectMeta.systems.RenderingSystem;
import com.mygdx.projectMeta.utils.Constants;
import com.mygdx.projectMeta.utils.WorldUtils;

import javax.xml.soap.Text;

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
        Entity actionText = createActionTextEntity();
        Entity player = createPlayer();
        createPlayerTorso(player);
        createCouch();
        createTV();
        createToilet();
        createBathtub(player, actionText);

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
        SoundComponent soundComponent = new SoundComponent();

        physicsComponent.body = WorldUtils.createPlayer(world);
        physicsComponent.userData = new RunnerUserData();

        stateComponent.set(PlayerComponent.STATE_STILL);
        animationComponent.animations.put(PlayerComponent.STATE_WALKING, Assets.playerWalking);
        animationComponent.animations.put(PlayerComponent.STATE_STILL, Assets.playerStill);

        soundComponent.sound.add(Assets.slipperStepsSound);

        entity.add(textureComponent);
        entity.add(transformComponent);
        entity.add(inputComponent);
        entity.add(physicsComponent);
        entity.add(animationComponent);
        entity.add(stateComponent);
        entity.add(playerComponent);
        entity.add(soundComponent);

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
        StateComponent stateComponent = new StateComponent();
        AnimationComponent animationComponent = new AnimationComponent();

        physicsComponent.body = WorldUtils.createTV(world);
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

    private Entity createBathtub(Entity triggerEntity, Entity actionText)
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
        SoundComponent soundComponent = new SoundComponent();

        triggerComponent.range = 4;
        triggerComponent.triggerer = triggerEntity;
        triggerComponent.actionTextEntity = actionText;
        physicsComponent.body = WorldUtils.createStaticFurniture(world, Constants.BATHTUB_X, Constants.BATHTUB_Y, Constants.BATHTUB_WIDTH, Constants.BATHTUB_HEIGHT);
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

    public Entity createActionTextEntity()
    {
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
