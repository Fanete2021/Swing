package src.entity.transport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bike extends Transport {
    public static Image image;
    public static float frequency = 0.5f;
    public static float generationTime = 1f;
    public static int count = 0;
    public static boolean isMoving = true;

    public Bike(int x, int y, float timeBirth, float lifetime) {
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

    public Bike clone() {
        return new Bike(x, y, timeBirth, lifetime);
    }
}
