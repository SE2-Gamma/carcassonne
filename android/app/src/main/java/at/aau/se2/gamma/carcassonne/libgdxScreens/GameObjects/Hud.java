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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {
    private Stage stage;
    private Viewport viewport;
    private String olddegubText = "";
    private SpriteBatch sb;

    private Label statusTestLabel;
    private Label counterTestLabel;
    private Table table;
    private Label.LabelStyle labelstyle;
    private Texture errorTexture = new Texture("testTexture.jpg");
    private Texture currentTexture;
    private Image errorImg = new Image(errorTexture);
    private Table previewTable;
    private Image currentImg;

    float rotation;

    private int counter;


    public Hud(SpriteBatch sb){
        this.sb = sb;
        viewport = new ScreenViewport();
        stage = new Stage(viewport, sb);



        table = new Table(); //table for layout management
        table.top(); //aligns item on top
        table.setFillParent(true); //set table to size of stage

        labelstyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        statusTestLabel = new Label(" ", labelstyle);
        statusTestLabel.setFontScale(5f);

        //expandX --> label takes up a complete row of the screen
        //pad(10) 10px padding
        table.add(statusTestLabel).expandX().pad(10);
        table.row(); //creates a new row

        counterTestLabel = new Label(""+counter, labelstyle);
        counterTestLabel.setFontScale(5f);
        table.add(counterTestLabel);

        Image arrowImage_left = new Image(new Texture("Arrow_left_thiccc.png"));
        Image arrowImage_right = new Image(new Texture("Arrow_right_thiccc.png"));

        stage.addActor(table);

        currentImg = errorImg;
        currentImg.setDrawable(new TextureRegionDrawable(new Texture("testTexture.jpg")));
        rotation = 0;

        //setting up preview table, for CardPreview in the UI/HUD
        previewTable = new Table();
        previewTable.add(arrowImage_left).align(Align.right).size(128,128);
        previewTable.row();
        previewTable.setFillParent(true);
        previewTable.bottom();
        previewTable.align(Align.left | Align.bottom).pad(30);
        currentImg.setScale(1);
        previewTable.add(currentImg).size(384, 384);

        previewTable.add(arrowImage_right).align(Align.top).size(128,128);

        stage.addActor(previewTable);
        counter = 0;

        //adding listeners to arrows for rotation
        arrowImage_left.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                counter++;
                rotation +=90;
                currentImg.addAction(Actions.parallel(Actions.rotateTo(rotation, 0.1f)));

            }
        });

        arrowImage_right.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                counter++;
                rotation -=90;
                currentImg.addAction(Actions.parallel(Actions.rotateTo(rotation, 0.1f)));
            }
        });

    }

    public void drawStage(String debugText){

        if(!debugText.equals(olddegubText)){
            //stage.clear();
            //table.clear();
            olddegubText = debugText;
            statusTestLabel.setText(debugText);

        }
        counterTestLabel.setText(""+counter +" | rotation "+rotation);

        currentImg.setOrigin(currentImg.getWidth()/2, currentImg.getHeight()/2);
        stage.act();
        stage.draw();
    }

    public void setNextCardTexture(Texture cardTexture){
        this.currentTexture = cardTexture;
        currentImg.setDrawable(new TextureRegionDrawable(currentTexture));
        //this.currentImg = new Image(this.currentTexture);
        rotation = 0;
        currentImg.setRotation(0);

    }

    public Camera returncam(){
        return stage.getCamera();
    }

    public void dispose(){
        stage.dispose();
    }

    public Texture getCurrentTexture() {
        return this.currentTexture;
    }

    public Stage getStage(){
        return stage;
    }

    public void setRotation (float rotation){
        this.rotation = rotation%360;
    }

    public float getRotation(){

        return rotation%360;
    }
}
