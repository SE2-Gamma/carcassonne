package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.math.Vector2;

import at.aau.se2.gamma.core.models.impl.Soldier;

public class CheatMoveSoldierPosition {
    private Vector2 position;
    private Soldier soldier;
    private GameCard gamecard;

    public CheatMoveSoldierPosition(Vector2 position, Soldier soldier, GameCard gamecard) {
        this.position = position;
        this.soldier = soldier;
        this.gamecard = gamecard;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Soldier getSoldier() {
        return soldier;
    }

    public void setSoldier(Soldier soldier) {
        this.soldier = soldier;
    }

    public GameCard getGamecard() {
        return gamecard;
    }

    public void setGamecard(GameCard gamecard) {
        this.gamecard = gamecard;
    }
}
