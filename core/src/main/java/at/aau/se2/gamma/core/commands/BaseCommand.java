package at.aau.se2.gamma.core.commands;

import java.io.Serializable;

abstract public class BaseCommand implements Serializable {
    abstract public Object run(Object payload); // ResponseObject // if app send command to server
    abstract public String getKey();
}
