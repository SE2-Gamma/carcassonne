package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class Hud_Item_CardPreview {

    private Texture errorTexture = new Texture("testTexture.jpg");
    private Texture currentTexture;
    private Image errorImg = new Image(errorTexture);
    private Table previewTable;
    private Image currentImg;

    float rotation;

    private int counter;


    public Hud_Item_CardPreview(){

        Image arrowImage_left = new Image(new Texture("Arrow_left_thiccc.png"));
        Image arrowImage_right = new Image(new Texture("Arrow_right_thiccc.png"));

        counter = 0;

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
        currentImg.setOrigin(currentImg.getWidth()/2, currentImg.getHeight()/2);

        //adding listeners to arrows for rotation
        arrowImage_left.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                addRotation_with_Animation(+90.0f);
                counter++;

            }
        });

        arrowImage_right.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                addRotation_with_Animation(-90.0f);
                counter++;
            }
        });

    }

    public void addRotation_with_Animation(float rotation){
        this.rotation += rotation;
        currentImg.setOrigin(currentImg.getWidth()/2, currentImg.getHeight()/2);
        currentImg.addAction(Actions.parallel(Actions.rotateTo(this.rotation, 0.1f)));
    }


    public void setRotation(float rotation){
        this.rotation = rotation;
        currentImg.setRotation(rotation);
    }

    public Table getTable(){
        return previewTable;
    }

    public void setCardPreviewTexutre(Texture preview){
        this.currentTexture = preview;
        currentImg.setDrawable(new TextureRegionDrawable(currentTexture));
    }

    public Texture getCardPreviewTexture(){
        return currentTexture;
    }

    public int getCounter() {
        return counter;
    }

    public float getRotation() {
        return rotation%360;
    }
}
