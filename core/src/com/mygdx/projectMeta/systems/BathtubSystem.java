package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.projectMeta.components.*;

/**
 * Created by Dan on 7/18/2015.
 */
public class BathtubSystem extends IteratingSystem {

    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TriggerComponent> tm;
    private ComponentMapper<InputComponent> im;
    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<SoundComponent> soundMapper;

    public BathtubSystem() {
        super(Family.getFor(BathtubComponent.class,
                StateComponent.class,
                TriggerComponent.class,
                InputComponent.class,
                AnimationComponent.class,
                SoundComponent.class));

        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TriggerComponent.class);
        im = ComponentMapper.getFor(InputComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
        soundMapper = ComponentMapper.getFor(SoundComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        StateComponent stateComponent = sm.get(entity);
        TriggerComponent triggerComponent = tm.get(entity);
        InputComponent inputComponent = im.get(entity);
        AnimationComponent animationComponent = am.get(entity);
        SoundComponent soundComponent = soundMapper.get(entity);

        Animation animation = animationComponent.animations.get(stateComponent.get());

        if (animation.getAnimationDuration() <= stateComponent.time) {
            if (stateComponent.get() == BathtubComponent.STATE_RUNNING) {
                stateComponent.set(BathtubComponent.STATE_RAN);
            } else if (stateComponent.get() == BathtubComponent.STATE_DRAINING) {
                stateComponent.set(BathtubComponent.STATE_DRAINED);
            }
        }

        if (triggerComponent.triggered
                && stateComponent.get() != BathtubComponent.STATE_RUNNING
                && stateComponent.get() != BathtubComponent.STATE_DRAINING
                && inputComponent.actionInput) {
            if (stateComponent.get() == BathtubComponent.STATE_RAN) {
                stateComponent.set(BathtubComponent.STATE_DRAINING);

                if (!soundComponent.sound.isEmpty()) ;
                {
                    soundComponent.sound.get(0).play();
                }
            } else {
                stateComponent.set(BathtubComponent.STATE_RUNNING);

                if (!soundComponent.sound.isEmpty()) {
                    soundComponent.sound.get(1).play();
                }
            }
        }
    }
}
