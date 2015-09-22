package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.projectMeta.components.*;

/**
 * Created by Dan on 9/21/2015.
 */
public class ToiletSystem extends IteratingSystem {

    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TriggerComponent> tm;
    private ComponentMapper<InputComponent> im;
    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<SoundComponent> soundMapper;

    public ToiletSystem() {
        super(Family.getFor(ToiletComponent.class,
                StateComponent.class,
                TriggerComponent.class,
                InputComponent.class,
                AnimationComponent.class));

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

        Animation animation = animationComponent.animations.get(stateComponent.get());

        if (animation.getAnimationDuration() <= stateComponent.time) {
            if (stateComponent.get() == ToiletComponent.STATE_FLUSHING) {
                stateComponent.set(ToiletComponent.STATE_IDLE);
            }
        }

        if (triggerComponent.triggered
                && stateComponent.get() != ToiletComponent.STATE_FLUSHING
                && inputComponent.actionInput) {

                stateComponent.set(ToiletComponent.STATE_FLUSHING);

                /*if (!soundComponent.sound.isEmpty()) {
                    soundComponent.sound.get(1).play();
                }*/
        }
    }
}
