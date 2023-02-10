package Clasess.Entity;


import Clasess.Core.Utils;
import Clasess.Emitter.ActionControl;
import Clasess.Emitter.Emitter;
import Clasess.Emitter.Events;
import Interfaces.IBehaviour;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Transport implements IBehaviour {
    protected int x, y;
    protected int timeBirth, lifetime;
    protected int id;

    public Transport(int x, int y, int timeBirth, int lifetime) {
        this.x = x;
        this.y = y;
        this.timeBirth = timeBirth;
        this.lifetime = lifetime;

        id = Utils.generateInteger(Integer.MAX_VALUE, 0);
    }

    public abstract Image getImage();

    public int getTimeBirth() {
        return timeBirth;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setTimeBirth(int timeBirth) {
        this.timeBirth = timeBirth;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public int getId() {
        return id;
    }
}
