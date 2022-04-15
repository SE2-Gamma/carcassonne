package at.aau.se2.gamma.core.models.impl;

import at.aau.se2.gamma.core.models.interfaces.PlayerInterface;

import java.io.Serializable;

public class Player extends BaseModel implements PlayerInterface, Serializable {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
