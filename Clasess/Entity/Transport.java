package Clasess.Entity;


import Clasess.Core.Utils;

import java.awt.*;

public abstract class Transport implements IMovable {
    protected int x, y;
    protected int timeBirth, lifetime;
    protected int id;
    protected int speed;

    public Transport(int x, int y, int timeBirth, int lifetime) {
        this.x = x;
        this.y = y;
        this.timeBirth = timeBirth;
        this.lifetime = lifetime;
        this.speed = 10;

        id = Utils.generateInteger(Integer.MAX_VALUE, 0);
    }

    public abstract Image getImage();

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTimeBirth() {
        return timeBirth;
    }

    public int getLifetime() {
        return lifetime;
    }

    public int getId() {
        return id;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
