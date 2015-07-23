package com.mygdx.projectMeta.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.projectMeta.ProjectMeta;
import com.mygdx.projectMeta.utils.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = Constants.APP_HEIGHT;
        config.width = Constants.APP_WIDTH;
        config.resizable = false;
		new LwjglApplication(new ProjectMeta(), config);
	}
}
