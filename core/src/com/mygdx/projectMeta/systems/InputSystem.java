package com.mygdx.projectMeta.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.projectMeta.components.InputComponent;
import com.mygdx.projectMeta.utils.Constants;

/**
 * Created by Dan on 7/18/2015.
 */
public class InputSystem extends IteratingSystem
{
    private boolean actionPressed;
    private int movementInput = 0;
    private Vector2 faceThis = new Vector2(0,0);
    private Camera camera = null;

    private ComponentMapper<InputComponent> mm;

    public InputSystem() {
        super(Family.getFor(InputComponent.class));

        mm = ComponentMapper.getFor(InputComponent.class);

        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int keycode) {

                if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                    startMoving(Constants.BACKWARD);
                }

                if(keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                    startMoving(Constants.FORWARD);
                }

                if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
                    startMoving(Constants.LEFT);
                }

                if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
                    startMoving(Constants.RIGHT);
                }

                return super.keyDown(keycode);
            }

            public boolean keyUp(int keycode) {

                if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                    stopMoving(Constants.BACKWARD);
                }

                if(keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                    stopMoving(Constants.FORWARD);
                }

                if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
                    stopMoving(Constants.LEFT);
                }

                if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
                    stopMoving(Constants.RIGHT);
                }

                return super.keyUp(keycode);
            }

        });
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        InputComponent inputComponent = mm.get(entity);

        actionPressed = (Gdx.input.isKeyPressed(Input.Keys.E));

        Vector3 point = new Vector3();
        camera.unproject(point.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        faceThis.set(point.x, point.y);

        inputComponent.faceThis = faceThis;
        inputComponent.movementInput = movementInput;
        inputComponent.actionInput = actionPressed;
    }

    public void startMoving(int input)
    {
        movementInput |= input;
    }

    public void stopMoving(int input)
    {
        if ((movementInput & input) != 0)
        {
            movementInput ^= input;
        }
    }
}
