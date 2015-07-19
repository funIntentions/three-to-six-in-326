package com.mygdx.projectMeta.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Dan on 7/18/2015.
 */
public class BathtubComponent extends Component {
    public static final int STATE_RUNNING = 0;
    public static final int STATE_RAN = 1;
    public static final int STATE_DRAINING = 2;
    public static final int STATE_DRAINED = 3;
}
