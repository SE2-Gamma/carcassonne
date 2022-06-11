package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import at.aau.se2.gamma.carcassonne.libgdxScreens.Screens.Gamescreen;
import at.aau.se2.gamma.core.models.impl.Player;

public class Hud {

    public enum Hud_State {
        VIEWING,
        PLAYING,
        ACCEPT_ACTION,
        CHEATING,
        REPORTING,
        PLACING_SOLDIER,
        ACCEPT_PLACING_SOLDIER,
        SCOREBOARD,
        ACCEPT_REPORTING,
        ACCEPT_CHEATING,
        CHEATING_DIRECTION

    }


    private Gamescreen myGamescreen;
    private Stage stage;
    private Viewport viewport;

    private Hud_Item_BottomText hudTopItem;
    private Hud_Item_CardPreview hudCardPreview;
    private Hud_Item_AcceptDeclineButtons accept_decline_buttons;
    private Hud_Item_ErrorText hud_errortext;
    private Hud_Item_AcceptDeclineButtons accept_decline_buttons_soldiers;
    private Hud_Item_ZeroSoldiersButton hud_ZeroSoldier_buttons;
    private Hud_Item_AcceptDeclineButtons accept_decline_buttons_report;
    private Hud_Item_ZeroSoldiersButton decline_soldier_cheat;
    private Hud_Item_Scoreboard hud_scoreboard;

    private TextButton stat_button;
    private TextButton play_button;
    private TextButton report_button;
    private TextButton cheat_button;

    private Hud_State currentState;
    private boolean myTurn;

    private boolean debugging;
    private Hud_State lastPlayingState;
    private ButtonGroup<TextButton> buttonGroupTextButtons;


    public Hud(SpriteBatch sb, Gamescreen gs) {
        myTurn=false;
        debugging = false;
        myGamescreen = gs;
        this.currentState = Hud_State.VIEWING;

        viewport = new ScreenViewport();
        stage = new Stage(viewport, sb);


        //creating all Hud Elements
        hudTopItem = new Hud_Item_BottomText();
        hudCardPreview = new Hud_Item_CardPreview();
        accept_decline_buttons = new Hud_Item_AcceptDeclineButtons();
        hud_errortext = new Hud_Item_ErrorText();
        hud_ZeroSoldier_buttons = new Hud_Item_ZeroSoldiersButton();
        accept_decline_buttons_soldiers = new Hud_Item_AcceptDeclineButtons();
        accept_decline_buttons_report = new Hud_Item_AcceptDeclineButtons("Report selected soldier", "Decline");
        hud_scoreboard = new Hud_Item_Scoreboard();

        changeHudState(Hud_State.PLAYING);

        //adding left Menu Buttons
        stat_button = new TextButton("STATS", UISkin.getSkin(), "toggle");
        play_button = new TextButton("PLAY", UISkin.getSkin(), "toggle");
        report_button = new TextButton("REPORT", UISkin.getSkin(), "toggle");
        cheat_button = new TextButton("CHEAT", UISkin.getSkin(), "toggle");

        stat_button.getLabel().setFontScale(5f);
        play_button.getLabel().setFontScale(5f);
        report_button.getLabel().setFontScale(5f);
        cheat_button.getLabel().setFontScale(5f);

        buttonGroupTextButtons = new ButtonGroup<>();
        buttonGroupTextButtons.add(stat_button);
        buttonGroupTextButtons.add(play_button);
        buttonGroupTextButtons.add(report_button);
        buttonGroupTextButtons.add(cheat_button);
        buttonGroupTextButtons.setMinCheckCount(1);
        buttonGroupTextButtons.setMaxCheckCount(1);
        buttonGroupTextButtons.setChecked("PLAY");
        buttonGroupTextButtons.setUncheckLast(true); //If true, when the maximum number of buttons are checked and an additional button is checked, the last button to be checked is unchecked so that the maximum is not exceeded.

        stat_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                buttonGroupTextButtons.setChecked("STATS");
            }
        });

        report_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                buttonGroupTextButtons.setChecked("REPORT");
            }
        });
        cheat_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                buttonGroupTextButtons.setChecked("CHEAT");
                //if(currentState.equals(Hud_State.PLAYING) || currentState.equals(Hud_State.VIEWING) || currentState.equals(Hud_State.ACCEPT_ACTION)|| currentState.equals(Hud_State.ACCEPT_PLACING_SOLDIER) || currentState.equals(Hud_State.PLACING_SOLDIER)){
                //  lastPlayingState = currentState;
                // }
                // changeHudState(Hud_State.CHEATING);

            }
        });
        play_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                buttonGroupTextButtons.setChecked("PLAY");
                // if(!(currentState.equals(Hud_State.PLAYING) || currentState.equals(Hud_State.VIEWING) || currentState.equals(Hud_State.ACCEPT_ACTION)|| currentState.equals(Hud_State.ACCEPT_PLACING_SOLDIER) || currentState.equals(Hud_State.PLACING_SOLDIER))){
                //  if(myTurn) changeHudState(Hud_State.PLAYING);
                // else changeHudState(Hud_State.VIEWING);
                // }


            }
        });

        decline_soldier_cheat = new Hud_Item_ZeroSoldiersButton("Decline Cheat");

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

                stage.addActor(hudCardPreview.getTable());

                break;
            case VIEWING:
                stage.clear();
                break;
            case CHEATING:
                stage.clear();
                break;
            case REPORTING:
                stage.clear();
                break;
            case ACCEPT_ACTION:
                stage.clear();
                stage.addActor(accept_decline_buttons.getButtonTable());
                break;
            case PLACING_SOLDIER:
                stage.clear();
                stage.addActor(hud_ZeroSoldier_buttons.getButtonTable());
                break;
            case ACCEPT_PLACING_SOLDIER:
                stage.clear();
                stage.addActor(accept_decline_buttons_soldiers.getButtonTable());
                break;
            case SCOREBOARD:
                stage.clear();
                stage.addActor(hud_scoreboard.getTable());
                break;
            case ACCEPT_REPORTING:
                stage.clear();
                stage.addActor(accept_decline_buttons_report.getButtonTable());
                break;
            case CHEATING_DIRECTION:
                stage.clear();
                break;
            case ACCEPT_CHEATING:
                stage.clear();
                stage.addActor(decline_soldier_cheat.getButtonTable());
                break;

        }
        stage.addActor(hudTopItem.getTable());
        stage.addActor(hud_errortext.getTable());


        if(debugging){

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
        buttonDebugTable.align(Align.right | Align.top);
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

        Table HudMenuButtonTable = new Table();
        HudMenuButtonTable.setFillParent(true);
        HudMenuButtonTable.align(Align.left | Align.top);

        HudMenuButtonTable.add(stat_button).pad(10);
        HudMenuButtonTable.add(play_button).pad(10);
        HudMenuButtonTable.add(report_button).pad(10);
        HudMenuButtonTable.add(cheat_button).pad(10);

        stage.addActor(HudMenuButtonTable);

    }

    public ButtonGroup getButtonGroup(){
        return buttonGroupTextButtons;
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

    public TextButton getPlay_button() {
        return play_button;
    }
    public TextButton getReport_button() {
        return report_button;
    }
    public TextButton getStat_button() {
        return stat_button;
    }

    public TextButton getCheat_button() {
        return cheat_button;
    }


        public TextButton getNoSoldierButton() {
        return hud_ZeroSoldier_buttons.getButton();
    }

    public Hud_State getCurrentState() {
        return currentState;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public TextButton getReportAcceptButton(){
        return accept_decline_buttons_report.getAcceptButton();
    }

    public TextButton getReportDeclineButton(){
        return accept_decline_buttons_report.getDeclineButton();
    }

    public TextButton getCheatDeclineButton(){
        return decline_soldier_cheat.getButton();
    }


    public void showErrorText(String text) {
        hud_errortext.setErrorText(text);
        if(hud_errortext.getTable().getActions().isEmpty()) {
            hud_errortext.getTable().addAction(Actions.sequence(Actions.delay(3f), Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    hud_errortext.setErrorText("");
                }
            }), Actions.alpha(1)));
        }
    }

    public void showInfoText(String text){
            hudTopItem.setStatusTextSecondRow(text);
            if(hudTopItem.getTable().getActions().isEmpty()){
                hudTopItem.getTable().addAction(Actions.sequence(Actions.delay(3f), Actions.fadeOut(0.5f),Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        hudTopItem.setStatusTextSecondRow("");
                    }
                }), Actions.alpha(1)));
            }
    }

    public void setHud_scoreboard(ArrayList<Player> players){
        hud_scoreboard.setText(players);
    }
}
