package com.mygdx.projectMeta.components;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Dan on 7/19/2015.
 */
public class TextComponent extends Component
{
    public static int STATE_DISPLAYING = 0;
    public static int STATE_HIDDEN = 1;

    public String text = null;
    public BitmapFont font = null;
    public float displayTime = 0;
}
