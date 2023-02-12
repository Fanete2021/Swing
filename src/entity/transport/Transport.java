package src.entity.transport;


import src.core.Utils;
import src.entity.transport.IMovable;

import java.awt.*;

public abstract class Transport implements IMovable {
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

    public abstract Image getImage();

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getTimeBirth() {
        return timeBirth;
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
