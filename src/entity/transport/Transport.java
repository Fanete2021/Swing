package src.entity.transport;

import src.utils.Utils;

import java.awt.*;
import java.io.Serializable;

public abstract class Transport implements IMovable, Serializable {
    protected int x, y;
    protected float timeBirth, lifetime;
    protected int id;
    protected int speed;

    public Transport(int x, int y, float timeBirth, float lifetime) {
        this.x = x;
        this.y = y;
        this.timeBirth = timeBirth;
        this.lifetime = lifetime;
        this.speed = 10;

        id = Utils.generateInteger(Integer.MAX_VALUE);
    }

    public abstract Transport clone();

    public abstract Image getImage();

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getTimeBirth() {
        return timeBirth;
    }

    public void setTimeBirth(float time) {
        timeBirth = time;
    }

    public float getLifetime() {
        return lifetime;
    }

    public boolean isDead(float time) { return time >= timeBirth + lifetime;}

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
