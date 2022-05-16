package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;


import android.graphics.Paint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class Hud_Item_AcceptDeclineButtons {

    private Table buttonTable;
    private TextButton acceptButton;
    private TextButton declineButton;
    private Skin skin;


    public Hud_Item_AcceptDeclineButtons(){
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        acceptButton = new TextButton("Accept", skin, "default");
        declineButton = new TextButton("Decline", skin, "default");
        acceptButton.getLabel().setFontScale(5f);
        declineButton.getLabel().setFontScale(5f);
        declineButton.pack();
        acceptButton.pack();
        buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.align(Align.right | Align.bottom).pad(10f);
        buttonTable.add(acceptButton).pad(10f);
        buttonTable.add(declineButton).pad(10f);

        acceptButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });

        declineButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });


    }

    public Table getButtonTable(){
        return buttonTable;
    }

    public TextButton getAcceptButton(){
        return acceptButton;
    }

    public TextButton getDeclineButton(){
        return declineButton;
    }



}
