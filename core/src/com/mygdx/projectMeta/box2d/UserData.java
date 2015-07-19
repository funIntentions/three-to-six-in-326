package com.mygdx.projectMeta.box2d;

import com.mygdx.projectMeta.enums.UserDataType;

/**
 * Created by Dan on 4/22/2015.
 */
public abstract class UserData {

    protected UserDataType userDataType;

    public UserData() {

    }

    public UserDataType getUserDataType() {
        return userDataType;
    }
}
