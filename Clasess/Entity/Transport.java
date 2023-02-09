package Clasess.Entity;

import java.awt.*;

public abstract class Transport {
    public int x, y;

    public Transport(int x, int y) {
        setX(x);
        setY(y);
    }

    public abstract Image getImage();

    public static float getFrequency() {
        return 0.0f;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
