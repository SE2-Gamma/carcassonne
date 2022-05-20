package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    private SpriteBatch sb;

    private Hud_Item_TopText hudTopItem;
    private Hud_Item_CardPreview hudCardPreview;
    private Hud_Item_AcceptDeclineButtons accept_decline_buttons;
    private Hud_Item_ErrorText hud_errortext;
    private Hud_Item_AcceptDeclineButtons accept_decline_buttons_soldiers;
    private Hud_Item_ZeroSoldiersButton hud_ZeroSoldier_buttons;

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
        hud_ZeroSoldier_buttons = new Hud_Item_ZeroSoldiersButton();
        accept_decline_buttons_soldiers = new Hud_Item_AcceptDeclineButtons();

        changeHudState(Hud_State.PLAYING);


    }

    public void drawStage() {
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
                stage.addActor(hudTopItem.getTable());
                break;
            case CHEATING:
                stage.clear();
                stage.addActor(hudTopItem.getTable());
                break;
            case REPORTING:
                stage.clear();
                stage.addActor(hudTopItem.getTable());
                break;
            case ACCEPT_ACTION:
                stage.clear();
                stage.addActor(accept_decline_buttons.getButtonTable());
                stage.addActor(hudTopItem.getTable());
                break;
            case PLACING_SOLDIER:
                stage.clear();
                stage.addActor(hud_ZeroSoldier_buttons.getButtonTable());
                stage.addActor(hudTopItem.getTable());
                break;
            case ACCEPT_PLACING_SOLDIER:
                stage.clear();
                stage.addActor(accept_decline_buttons_soldiers.getButtonTable());
                stage.addActor(hudTopItem.getTable());
                break;
            case SCOREBOARD:
                stage.clear();
                stage.addActor(hudTopItem.getTable());
                break;

        }

        stage.addActor(hud_errortext.getTable());

        //button ui test
        final TextButton button = new TextButton("VIEWING", UISkin.getSkin(), "default");
        final TextButton button2 = new TextButton("PLAYING", UISkin.getSkin(), "default");
        final TextButton button3 = new TextButton("Acc./Dec.", UISkin.getSkin(), "default");
        final TextButton button4 = new TextButton("ErrorTest", UISkin.getSkin(), "default");


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

    public TextButton getAcceptButton_Soldiers() {
        return accept_decline_buttons_soldiers.getAcceptButton();
    }

    public TextButton getDeclineButton_Soldiers() {
        return accept_decline_buttons_soldiers.getDeclineButton();
    }

    public TextButton getNoSoldierButton() {
        return hud_ZeroSoldier_buttons.getButton();
    }

    public Hud_State getCurrentState() {
        return currentState;
    }

    public void showErrorText(String text) {
        hud_errortext.setErrorText(text);
        hud_errortext.getTable().addAction(Actions.sequence(Actions.delay(3f), Actions.fadeOut(0.5f),Actions.run(new Runnable() {
            @Override
            public void run() {
                hud_errortext.setErrorText("");
            }
        }), Actions.alpha(1)));
    }

    public void showInfoText(String text){
            hudTopItem.setStatusTextFirstRow(text);
            hudTopItem.getTable().addAction(Actions.sequence(Actions.delay(3f), Actions.fadeOut(0.5f),Actions.run(new Runnable() {
            @Override
            public void run() {
                hudTopItem.setStatusTextFirstRow("");
            }
        }), Actions.alpha(1)));

    }
}
