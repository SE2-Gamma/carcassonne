package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class Hud_Item_MenuButtons {

    private TextButton stat_button;
    private TextButton play_button;
    private TextButton report_button;
    private TextButton cheat_button;

    private ButtonGroup<TextButton> buttonGroupTextButtons;

    private Table HudMenuButtonTable;

    private Skin skin;

    public Hud_Item_MenuButtons(){
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        //adding left Menu Buttons
        stat_button = new TextButton("STATS", skin, "toggle");
        play_button = new TextButton("PLAY", skin, "toggle");
        report_button = new TextButton("REPORT", skin, "toggle");
        cheat_button = new TextButton("CHEAT", skin, "toggle");

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

        HudMenuButtonTable = new Table();
        HudMenuButtonTable.setFillParent(true);
        HudMenuButtonTable.align(Align.left | Align.top);

        HudMenuButtonTable.add(stat_button).pad(10);
        HudMenuButtonTable.add(play_button).pad(10);
        HudMenuButtonTable.add(report_button).pad(10);
        HudMenuButtonTable.add(cheat_button).pad(10);
    }

    public TextButton getPlay_button() {
        return play_button;
    }

    public TextButton getReport_button() {
        return report_button;
    }

    public TextButton getCheat_button() {
        return cheat_button;
    }

    public TextButton getStat_button() {
        return stat_button;
    }

    public Table getHudMenuButtonTable() {
        return HudMenuButtonTable;
    }
}
