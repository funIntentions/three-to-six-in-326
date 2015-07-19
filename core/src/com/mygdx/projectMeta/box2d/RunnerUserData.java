package com.mygdx.projectMeta.box2d;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.projectMeta.enums.UserDataType;
import com.mygdx.projectMeta.utils.Constants;

/**
 * Created by Dan on 4/22/2015.
 */
public class RunnerUserData extends UserData {
    private float linearForce;
    private final Vector2 runningPosition = new Vector2(Constants.PLAYER_X, Constants.PLAYER_Y);

    public RunnerUserData() {
        super();
        linearForce = Constants.PLAYER_LINEAR_FORCE;
        userDataType = UserDataType.RUNNER;
    }

    public float getLinearForce() {
        return linearForce;
    }

    public void setJumpingLinearImpulse(float linearForce) {
        this.linearForce = linearForce;
    }

    public Vector2 getRunningPosition() {
        return runningPosition;
    }

}
