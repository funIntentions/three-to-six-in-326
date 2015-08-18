package com.mygdx.projectMeta.box2d;

import com.badlogic.ashley.core.Entity;
import com.mygdx.projectMeta.enums.UserDataType;

/**
 * Created by Dan on 8/18/2015.
 */
public class HandUserData extends UserData {

    Entity entityTouching = null;

    public HandUserData() {
        super();

        userDataType = UserDataType.HAND;
    }

    public Entity getEntityTouching() {
        return entityTouching;
    }

    public void setEntityTouching(Entity entityTouching) {
        this.entityTouching = entityTouching;
    }
}
