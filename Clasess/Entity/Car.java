package Clasess.Entity;

import Interfaces.IBehaviour;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Car extends Transport implements IBehaviour {
    public static Image image;
    public static float frequency = 1.0f;
    public static int generationTime = 1000;
    public static int count = 0;

    public Car(int x, int y) {
        super(x, y);
        Car.count++;
    }

    public Image getImage() {
        return Car.image;
    }

    public static void setImage(String path) {
        try {
            Car.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
