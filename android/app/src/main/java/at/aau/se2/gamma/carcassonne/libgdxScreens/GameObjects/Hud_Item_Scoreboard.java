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

    private Label Heading;
    private Label Player1Score;
    private Label Player2Score;
    private Label Player3Score;
    private Label Player4Score;
    private Label Player5Score;

    public Hud_Item_Scoreboard(){
        table = new Table();
        table.setFillParent(true); //set table to size of stage
        labelstyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);


        Heading = new Label(""+prepareString("Names")+" | "+"Points", labelstyle);
        Heading.setFontScale(5f);
        table.add(Heading).expandX().pad(10);
        table.row();


        Player1Score = new Label(""+prepareString("")+" | 0", labelstyle);
        Player1Score.setFontScale(5f);
        table.add(Player1Score).expandX().pad(10);
        table.row();

        Player2Score = new Label(""+prepareString("")+" | 0", labelstyle);
        Player2Score.setFontScale(5f);
        table.add(Player2Score).expandX().pad(10);
        table.row();

        Player3Score = new Label(""+prepareString("")+" | 0", labelstyle);
        Player3Score.setFontScale(5f);
        table.add(Player3Score).expandX().pad(10);
        table.row();

        Player4Score = new Label(""+prepareString("")+" | 0", labelstyle);
        Player4Score.setFontScale(5f);
        table.add(Player4Score).expandX().pad(10);
        table.row();

        Player5Score = new Label(""+prepareString("")+" | 0", labelstyle);
        Player5Score.setFontScale(5f);
        table.add(Player5Score).expandX().pad(10);
        table.row();
        table.align(Align.left);
    }

    public void setText(ArrayList<Player> players){
        int i = 0;
        for(Player p : players){
            switch (i){
                case 0:
                    Player1Score.setText(""+prepareString(p.getName())+" | "+p.getPlayerPoints().getPoints());
                    i++;
                    break;
                case 1:
                    Player2Score.setText(""+prepareString(p.getName())+" | "+p.getPlayerPoints().getPoints());
                    i++;
                    break;
                case 2:
                    Player3Score.setText(""+prepareString(p.getName())+" | "+p.getPlayerPoints().getPoints());
                    i++;
                    break;
                case 3:
                    Player4Score.setText(""+prepareString(p.getName())+" | "+p.getPlayerPoints().getPoints());
                    i++;
                    break;
                case 4:
                    Player5Score.setText(""+prepareString(p.getName())+" | "+p.getPlayerPoints().getPoints());
                    i++;
                    break;
            }
        }
    }

    public Table getTable(){
        return table;
    }

    public static String prepareString(String input){
        return String.format("%10.10s", input);
    }

}
