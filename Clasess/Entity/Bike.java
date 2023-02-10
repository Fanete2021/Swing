package Clasess.Entity;

import Clasess.Emitter.Emitter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bike extends Transport {
    public static Image image;
    public static float frequency = 0.5f;
    public static int generationTime = 1000;
    public static int count = 0;

    public Bike(int x, int y, int timeBirth, int lifetime) {
        super(x, y, timeBirth, lifetime);
        Bike.count++;
    }

    public Image getImage() {
        return Bike.image;
    }

    public static void setImage(String path) {
        try {
            Bike.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getX() {
        return super.x;
    }

    public int getY() {
        return super.y;
    }
}
