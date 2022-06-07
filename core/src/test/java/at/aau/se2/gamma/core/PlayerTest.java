package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.models.impl.Player;
import at.aau.se2.gamma.core.models.impl.PlayerPoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    Player player;

    @BeforeEach
    public void beforeTest() {
        player = new Player("person", "Person");
    }

    @Test
    public void addPoints() {
        player.addPlayerPoints(5);
        player.addPlayerPoints(-1);
        assertEquals(player.getPoints(), 4);
    }
}
