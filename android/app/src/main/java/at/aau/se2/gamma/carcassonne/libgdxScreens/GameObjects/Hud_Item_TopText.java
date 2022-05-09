package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Hud_Item_TopText {

    private Table table;
    private Label statusFirstRowLabel;
    private Label statusSecondRowLabel;

    private Label.LabelStyle labelstyle;

    public Hud_Item_TopText() {
        table = new Table(); //table for layout management
        table.top(); //aligns item on top
        table.setFillParent(true); //set table to size of stage

        labelstyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        statusFirstRowLabel = new Label("", labelstyle);
        statusFirstRowLabel.setFontScale(5f);

        table.add(statusFirstRowLabel).expandX().pad(10);

        table.row();

        statusSecondRowLabel = new Label("", labelstyle);
        statusSecondRowLabel.setFontScale(5f);
        table.add(statusSecondRowLabel);
    }

    public Table getTable() {
        return table;
    }

    public void setStatusTextFirstRow(String statusText) {
        this.statusFirstRowLabel.setText(statusText);
    }

    public void setStatusTextSecondRow(String statusText) {
        this.statusSecondRowLabel.setText(statusText);
    }

}
