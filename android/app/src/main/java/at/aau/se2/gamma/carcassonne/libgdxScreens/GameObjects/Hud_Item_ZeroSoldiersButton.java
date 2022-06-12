package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class Hud_Item_ZeroSoldiersButton {

    private Table buttonTable;
    private TextButton Button;
    private Skin skin;


    public Hud_Item_ZeroSoldiersButton(){
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        Button = new TextButton("Place no soldier", skin, "default");
        Button.getLabel().setFontScale(5f);
        Button.pack();
        buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.align(Align.right | Align.bottom).pad(10f);
        buttonTable.add(Button).pad(10f);

    }

    public Hud_Item_ZeroSoldiersButton(String text){
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        Button = new TextButton(text, skin, "default");
        Button.getLabel().setFontScale(5f);
        Button.pack();
        buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.align(Align.right | Align.bottom).pad(10f);
        buttonTable.add(Button).pad(10f);

    }

    public Table getButtonTable(){
        return buttonTable;
    }

    public TextButton getButton(){
        return Button;
    }



}
