package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.projectMeta.components.StateComponent;
import com.mygdx.projectMeta.components.TextComponent;

/**
 * Created by Dan on 7/19/2015.
 */
public class TextSystem extends IteratingSystem {
    private ComponentMapper<TextComponent> textMapper;
    private ComponentMapper<StateComponent> stateMapper;

    public TextSystem() {
        super(Family.getFor(TextComponent.class,
                StateComponent.class));

        textMapper = ComponentMapper.getFor(TextComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TextComponent text = textMapper.get(entity);
        StateComponent state = stateMapper.get(entity);

        if (state.get() == TextComponent.STATE_DISPLAYING) {
            if (state.time > text.displayTime) {
                state.set(TextComponent.STATE_HIDDEN);
            }
        }

        state.time += deltaTime;
    }
}
