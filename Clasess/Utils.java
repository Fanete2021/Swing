package Clasess;

import java.util.Random;

public class Utils {
    private Utils() {}

    private static Random random = new Random();

    public static boolean checkChance(float frequency) {
        float chance = random.nextFloat();
        return frequency >= chance;
    }

    public static int generateInteger(int bound, int minInt) {
        return random.nextInt(bound) + minInt;
    }
}
