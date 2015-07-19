package com.mygdx.projectMeta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 7/19/2015.
 */
public class SoundComponent extends Component {
    public List<Sound> sound = new ArrayList<Sound>();
}
