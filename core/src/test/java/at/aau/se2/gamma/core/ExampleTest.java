package at.aau.se2.gamma.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import at.aau.se2.gamma.core.models.impl.Player;

public class ExampleTest {
    // this is for the git demo
    @Test
    public void failure() {
        assertEquals(4, 2+2);
    }

    @Test
    public void player() {
        Player player = new Player("t1", "t2");
        player.setId("1sd");
        assertEquals("1sd", player.getId());
    }


}
