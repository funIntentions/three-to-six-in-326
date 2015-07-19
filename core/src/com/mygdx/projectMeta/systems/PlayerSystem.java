package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.projectMeta.components.*;

/**
 * Created by Dan on 7/18/2015.
 */
public class PlayerSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> pm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<InputComponent> mm;
    private ComponentMapper<SoundComponent> soundMapper;

    public PlayerSystem() {
        super(Family.getFor(PlayerComponent.class,
                StateComponent.class,
                TransformComponent.class,
                InputComponent.class,
                SoundComponent.class));

        pm = ComponentMapper.getFor(PlayerComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(InputComponent.class);
        soundMapper = ComponentMapper.getFor(SoundComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PlayerComponent playerComponent = pm.get(entity);
        StateComponent stateComponent = sm.get(entity);
        InputComponent inputComponent = mm.get(entity);
        SoundComponent soundComponent = soundMapper.get(entity);

        if (stateComponent.get() == PlayerComponent.STATE_WALKING
                && inputComponent.movementInput == 0)
        {
            stateComponent.set(PlayerComponent.STATE_STILL);

            if (!soundComponent.sound.isEmpty())
            {
                soundComponent.sound.get(0).stop();
            }
        }
        else if (stateComponent.get() != PlayerComponent.STATE_WALKING
                && inputComponent.movementInput != 0)
        {
            stateComponent.set(PlayerComponent.STATE_WALKING);

            if (!soundComponent.sound.isEmpty())
            {
                soundComponent.sound.get(0).loop();
            }
        }
    }
}
