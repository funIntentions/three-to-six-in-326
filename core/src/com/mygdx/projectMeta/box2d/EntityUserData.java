package com.mygdx.projectMeta.box2d;

import com.badlogic.ashley.core.Entity;
import com.mygdx.projectMeta.enums.UserDataType;

/**
 * Created by Dan on 8/18/2015.
 */
public class EntityUserData extends UserData {

    private Entity entity;

    public EntityUserData(Entity entity) {
        super();

        userDataType = UserDataType.ENTITY;
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
