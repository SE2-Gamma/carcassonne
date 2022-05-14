package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import android.graphics.fonts.Font;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import at.aau.se2.gamma.carcassonne.libgdxScreens.Screens.Gamescreen;

public class Hud {

    public enum Hud_State {
        VIEWING,
        PLAYING,
        ACCEPT_ACTION,
        CHEATING,
        REPORTING,
        PLACING_SOLDIER,
        ACCEPT_PLACING_SOLDIER,
        SCOREBOARD
    }


    private Gamescreen myGamescreen;
    private Stage stage;
    private Viewport viewport;
    private String olddegubText = "";
    private SpriteBatch sb;

    private Hud_Item_TopText hudTopItem;
    private Hud_Item_CardPreview hudCardPreview;
    private Hud_Item_AcceptDeclineButtons accept_decline_buttons;
    private Hud_Item_ErrorText hud_errortext;

    private Skin skin;
    private Hud_State currentState;


    public Hud(SpriteBatch sb, Gamescreen gs) {
        myGamescreen = gs;
        this.currentState = Hud_State.VIEWING;

        this.sb = sb;
        viewport = new ScreenViewport();
        stage = new Stage(viewport, sb);


        //creating all Hud Elements
        hudTopItem = new Hud_Item_TopText();
        hudCardPreview = new Hud_Item_CardPreview();
        accept_decline_buttons = new Hud_Item_AcceptDeclineButtons();
        hud_errortext = new Hud_Item_ErrorText();

        changeHudState(Hud_State.PLAYING);

    }

    public void drawStage(String debugText) {

        if (!debugText.equals(olddegubText)) {
            //stage.clear();
            //table.clear();
            olddegubText = debugText;
            hudTopItem.setStatusTextFirstRow(olddegubText);

        }
        hudTopItem.setStatusTextSecondRow("" + hudCardPreview.getCounter() + " | rotation " + hudCardPreview.getRotation());

        stage.act();
        stage.draw();
    }

    public void changeHudState(Hud_State state) {
        this.currentState = state;

        switch (state) {
            case PLAYING:
                stage.clear();

                stage.addActor(hudTopItem.getTable());
                stage.addActor(hudCardPreview.getTable());

                break;
            case VIEWING:
                stage.clear();
                break;
            case CHEATING:
                break;
            case REPORTING:
                break;
            case ACCEPT_ACTION:
                stage.clear();
                stage.addActor(accept_decline_buttons.getButtonTable());
                break;
            case PLACING_SOLDIER:
                break;
            case ACCEPT_PLACING_SOLDIER:
                break;
            case SCOREBOARD:
                break;

        }

        stage.addActor(hud_errortext.getTable());

        //button ui test

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        final TextButton button = new TextButton("VIEWING", skin, "default");
        final TextButton button2 = new TextButton("PLAYING", skin, "default");
        final TextButton button3 = new TextButton("Acc./Dec.", skin, "default");
        final TextButton button4 = new TextButton("ErrorTest", skin, "default");


        //button.setWidth(1000f);
        //button.setHeight(500f);
        //button.setPosition(Gdx.graphics.getWidth() /2 - 500f, Gdx.graphics.getHeight()/2 - 250f);
        button.getLabel().setFontScale(5f);
        button.pack();
        button2.getLabel().setFontScale(5f);
        button2.pack();
        button3.getLabel().setFontScale(5f);
        button3.pack();
        button4.getLabel().setFontScale(5f);
        button4.pack();

        Table buttonDebugTable = new Table();
        buttonDebugTable.setFillParent(true);
        buttonDebugTable.align(Align.left | Align.top);
        buttonDebugTable.add(button).pad(10);
        buttonDebugTable.row();
        buttonDebugTable.add(button2).pad(10);

        buttonDebugTable.row();
        buttonDebugTable.add(button3).pad(10);
        buttonDebugTable.row();
        buttonDebugTable.add(button4).pad(10);

        stage.addActor(buttonDebugTable);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //fadeOut also Changes map... NICE
                //button.addAction(Actions.fadeOut(0.5f));
                //button.remove();
                changeHudState(Hud_State.VIEWING);
            }
        });

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                changeHudState(Hud_State.PLAYING);
            }
        });

        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                changeHudState(Hud_State.ACCEPT_ACTION);
            }
        });

        button4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                hud_errortext.setErrorText("Couldn't place Tile");
                hud_errortext.getTable().addAction(Actions.sequence(Actions.delay(3f), Actions.fadeOut(0.5f),Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        hud_errortext.setErrorText("");
                    }
                }), Actions.alpha(1)));

            }
        });


    }


    public void setNextCardTexture(Texture cardTexture) {
        hudCardPreview.setCardPreviewTexutre(cardTexture);
        hudCardPreview.setRotation(0);
    }

    public Camera returncam() {
        return stage.getCamera();
    }

    public void dispose() {
        stage.dispose();
    }

    public Texture getCurrentTexture() {
        return hudCardPreview.getCardPreviewTexture();
    }

    public Stage getStage() {
        return stage;
    }

    public void setRotation(float rotation) {
        hudCardPreview.setRotation(rotation);
    }

    public float getRotation() {
        return hudCardPreview.getRotation();
    }

    public TextButton getAcceptButton() {
        return accept_decline_buttons.getAcceptButton();
    }

    public TextButton getDeclineButton() {
        return accept_decline_buttons.getDeclineButton();
    }

    public Hud_State getCurrentState() {
        return currentState;
    }

    public void showErrorText() {

    }
}
