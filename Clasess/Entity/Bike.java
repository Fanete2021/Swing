package Clasess.Entity;

import Interfaces.IBehaviour;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bike extends Transport implements IBehaviour {
    public static Image image;
    public static float frequency = 0.5f;
    public static int generationTime = 1000;
    public static int count = 0;

    public Bike(int x, int y) {
        super(x, y);
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
}
