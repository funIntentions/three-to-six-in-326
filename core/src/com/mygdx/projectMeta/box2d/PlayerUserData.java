package com.mygdx.projectMeta.box2d;

import com.mygdx.projectMeta.enums.UserDataType;
import com.mygdx.projectMeta.utils.Constants;

/**
 * Created by Dan on 4/22/2015.
 */
public class PlayerUserData extends UserData {
    private float linearForce;

    public PlayerUserData() {
        super();
        linearForce = Constants.PLAYER_LINEAR_FORCE;
        userDataType = UserDataType.PLAYER;
    }

    public float getLinearForce() {
        return linearForce;
    }

}
