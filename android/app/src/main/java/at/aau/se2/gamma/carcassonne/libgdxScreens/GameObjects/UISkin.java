package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UISkin {

    private static Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

    public static Skin getSkin(){
        return skin;
    }

    public static void disposeSkin(){
        skin.dispose();
    }
}
