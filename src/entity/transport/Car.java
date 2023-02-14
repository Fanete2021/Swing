package src.entity.transport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Car extends Transport {
    public static Image image;
    public static float frequency = 1f;
    public static float generationTime = 1f;
    public static int count = 0;
    public static boolean isMoving = true;

    public Car(int x, int y, float timeBirth, float lifetime) {
        super(x, y, timeBirth, lifetime);
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

    public Car clone() {
        return new Car(x, y, timeBirth, lifetime);
    }
}
