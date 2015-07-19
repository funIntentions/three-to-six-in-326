package com.mygdx.projectMeta.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.projectMeta.box2d.UserData;
import com.mygdx.projectMeta.enums.UserDataType;

/**
 * Created by Dan on 4/22/2015.
 */
public class BodyUtils {
    public static boolean bodyIsRunner(Body body) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.RUNNER;
    }

    public static boolean bodyIsGround(Body body) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.GROUND;
    }
}
