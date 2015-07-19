package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.mygdx.projectMeta.Assets;
import com.mygdx.projectMeta.components.StateComponent;
import com.mygdx.projectMeta.components.TextComponent;
import com.mygdx.projectMeta.components.TransformComponent;
import com.mygdx.projectMeta.utils.Constants;
import com.badlogic.gdx.scenes.scene2d.Stage;


/**
 * Created by Dan on 7/19/2015.
 */
public class TextRenderingSystem extends IteratingSystem
{
    private SpriteBatch batch;
    private Array<Entity> renderQueue;
    //private Stage stage;
    //private Label label;
    //private Label.LabelStyle style;
    //private Container<Label> container;

    private ComponentMapper<TextComponent> textMapper;
    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<StateComponent> stateMapper;

    public TextRenderingSystem(SpriteBatch batch) {
        super(Family.getFor(TransformComponent.class, TextComponent.class, StateComponent.class));

        textMapper = ComponentMapper.getFor(TextComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);

        renderQueue = new Array<Entity>();

        this.batch = batch;
        //stage = new Stage();
        //style = new Label.LabelStyle(Assets.journalFont, Color.ORANGE);
        //label = new Label("", style);
        //container = new Container<Label>(label);
        //stage.addActor(container);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        batch.begin();
        for (Entity entity : renderQueue) {
            TextComponent text = textMapper.get(entity);

            if (text.font == null || text.text == null) {
                continue;
            }

            TransformComponent transform = transformMapper.get(entity);
            StateComponent stateComponent = stateMapper.get(entity);

            //container.setPosition(transform.position.x, transform.position.y);
            //label.setText(text.text);

            if (stateComponent.get() == TextComponent.STATE_DISPLAYING)
                text.font.draw(batch, text.text, transform.position.x * Constants.PIXELS_PER_UNIT, transform.position.y * Constants.PIXELS_PER_UNIT);
        }

        //stage.draw();
        batch.end();
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }
}
