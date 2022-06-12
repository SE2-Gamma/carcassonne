package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import at.aau.se2.gamma.core.models.impl.Player;

public class Hud_Item_Scoreboard {
    private Table table;
    private Label.LabelStyle labelstyle;

    private Label HeadingName;
    private Label HeadingScore;
    private Label Line;

    private Label Player1Name;
    private Label Player1Score;
    private Label Line1;

    private Label Player2Name;
    private Label Player2Score;
    private Label Line2;

    private Label Player3Name;
    private Label Player3Score;
    private Label Line3;

    private Label Player4Name;
    private Label Player4Score;
    private Label Line4;

    private Label Player5Name;
    private Label Player5Score;
    private Label Line5;

    public Hud_Item_Scoreboard(){
        table = new Table();
        table.setFillParent(true); //set table to size of stage
        labelstyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        HeadingName = new Label(""+prepareString("Names"), labelstyle);
        HeadingScore = new Label("Points", labelstyle);
        Line = new Label("|", labelstyle);
        Line.setFontScale(5f);
        HeadingName.setFontScale(5f);
        HeadingScore.setFontScale(5f);
        table.add(HeadingName).pad(10);//.expandX().pad(10);
        table.add(Line).pad(10);
        table.add(HeadingScore).pad(10);
        table.row();

        Player1Name = new Label(""+prepareString(""), labelstyle);
        Player1Score = new Label("", labelstyle);
        Line1 = new Label("", labelstyle);
        Line1.setFontScale(5f);
        Player1Name.setFontScale(5f);
        Player1Score.setFontScale(5f);
        table.add(Player1Name).pad(10);//.expandX().pad(10);
        table.add(Line1).pad(10);
        table.add(Player1Score).pad(10);
        table.row();

        Player2Name = new Label(""+prepareString(""), labelstyle);
        Player2Score = new Label("", labelstyle);
        Line2 = new Label("", labelstyle);
        Line2.setFontScale(5f);
        Player2Name.setFontScale(5f);
        Player2Score.setFontScale(5f);
        table.add(Player2Name).pad(10);//.expandX().pad(10);
        table.add(Line2).pad(10);
        table.add(Player2Score).pad(10);
        table.row();

        Player3Name = new Label(""+prepareString(""), labelstyle);
        Player3Score = new Label("", labelstyle);
        Line3 = new Label("", labelstyle);
        Line3.setFontScale(5f);
        Player3Name.setFontScale(5f);
        Player3Score.setFontScale(5f);
        table.add(Player3Name).pad(10);//.expandX().pad(10);
        table.add(Line3).pad(10);
        table.add(Player3Score).pad(10);
        table.row();

        Player4Name = new Label(""+prepareString(""), labelstyle);
        Player4Score = new Label("", labelstyle);
        Line4 = new Label("", labelstyle);
        Line4.setFontScale(5f);
        Player4Name.setFontScale(5f);
        Player4Score.setFontScale(5f);
        table.add(Player4Name).pad(10);//.expandX().pad(10);
        table.add(Line4).pad(10);
        table.add(Player4Score).pad(10);
        table.row();

        Player5Name = new Label(""+prepareString(""), labelstyle);
        Player5Score = new Label("", labelstyle);
        Line5 = new Label("", labelstyle);
        Line5.setFontScale(5f);
        Player5Name.setFontScale(5f);
        Player5Score.setFontScale(5f);
        table.add(Player5Name).pad(10);//.expandX().pad(10);
        table.add(Line5).pad(10);
        table.add(Player5Score).pad(10);
        table.row();
    }

    public void setText(ArrayList<Player> players){
        int i = 0;
        for(Player p : players){
            switch (i){
                case 0:
                    Player1Name.setText(prepareString(p.getName()));
                    Player1Score.setText(p.getPlayerPoints().getPoints());
                    Line1.setText("|");

                    i++;
                    break;
                case 1:
                    Player2Name.setText(prepareString(p.getName()));
                    Player2Score.setText(p.getPlayerPoints().getPoints());
                    Line2.setText("|");
                    i++;
                    break;
                case 2:
                    Player3Name.setText(prepareString(p.getName()));
                    Player3Score.setText(p.getPlayerPoints().getPoints());
                    Line3.setText("|");
                    i++;
                    break;
                case 3:
                    Player4Name.setText(prepareString(p.getName()));
                    Player4Score.setText(p.getPlayerPoints().getPoints());
                    Line4.setText("|");
                    i++;
                    break;
                case 4:
                    Player5Name.setText(prepareString(p.getName()));
                    Player5Score.setText(p.getPlayerPoints().getPoints());
                    Line5.setText("|");
                    i++;
                    break;
            }
        }
    }

    public Table getTable(){
        return table;
    }

    public static String prepareString(String input){
        return String.format("%.10s", input);
    }

}
