package Clasess.Core;

import Clasess.Entity.Bike;
import Clasess.Entity.Car;

import java.util.Random;
import java.util.Vector;

public class Utils {
    private Utils() {}

    private static final Random random = new Random();

    public static boolean checkChance(float frequency) {
        float chance = random.nextFloat();
        return frequency >= chance;
    }

    public static int generateInteger(int bound, int minInt) {
        return random.nextInt(bound) + minInt;
    }

    public static Vector<String> generatePercentages(int start, int end, int step) {
        Vector<String> vector = new Vector<String>();

        for (int i = start; i <= end; i += step) {
            vector.add(i + "%");
        }

        return vector;
    }

    public static float parsePercentages(String str) {
        str = str.replace("%", "");

        return Float.parseFloat(str) / 100;
    }

    public static String joinArray(String[] arr) {
        String result = "";

        for (int i = 0; i < arr.length; ++i) {
            result += arr[i] + "\n";
        }

        return result;
    }

    public static String toStringTime(int time) {
        int seconds = (int)(time / 1000);
        int ms = (int)(time % 1000);

        return seconds + "." + ms / 100;
    }
}
