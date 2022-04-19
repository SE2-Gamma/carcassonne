package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    private Table table;
    private Label.LabelStyle labelstyle;
    private Texture errorTexture = new Texture("testTexture.jpg");
    private Texture currentTexture;
    private Image errorImg = new Image(errorTexture);
    private Table previewTable;
    private Image currentImg;


    public Hud(SpriteBatch sb){
        this.sb = sb;
        viewport = new ScreenViewport();
                //new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
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


        stage.addActor(table);

        currentImg = errorImg;
        currentImg.setDrawable(new TextureRegionDrawable(new Texture("testTexture.jpg")));
        //currentImg = new Image(errorTexture);
        //currentImg.setSize(384,384);
        //add preview image
        previewTable = new Table();
        previewTable.setFillParent(true);
        previewTable.bottom();
        previewTable.align(Align.left | Align.bottom).pad(30);
        currentImg.setScale(2);
        previewTable.add(currentImg);

        stage.addActor(previewTable);

    }

    public void drawStage(String debugText){

        if(!debugText.equals(olddegubText)){
            //stage.clear();
            //table.clear();
            olddegubText = debugText;
            statusTestLabel.setText(debugText);

        }
        //stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void setNextCardTexture(Texture cardTexture){
        this.currentTexture = cardTexture;
        currentImg.setDrawable(new TextureRegionDrawable(currentTexture));
        //this.currentImg = new Image(this.currentTexture);
    }

    //public

    public Camera returncam(){
        return stage.getCamera();
    }

    public void dispose(){
        stage.dispose();
    }

    public Texture getCurrentTexture() {
        return this.currentTexture;
    }
}
