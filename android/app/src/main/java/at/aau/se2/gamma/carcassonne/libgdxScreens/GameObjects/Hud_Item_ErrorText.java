package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Hud_Item_ErrorText {

    private Table table;
    private Label errorLabel;
    private Label.LabelStyle labelstyle;


    public Hud_Item_ErrorText() {
        table = new Table();
        table.setFillParent(true); //set table to size of stage
        labelstyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        table.center();
        errorLabel = new Label("", labelstyle);
        errorLabel.setFontScale(5f);
        table.add(errorLabel).expandX().pad(10);


    }

    public void setErrorText(String message) {
        errorLabel.setText(message);
    }


    public Table getTable() {
        return table;
    }
}
