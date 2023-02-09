import Clasess.Entity.Bike;
import Clasess.Entity.Car;
import Clasess.Core.Habitat;

public class Main {
    public static void main(String[] args) {
        Car.setImage("images/car.png");
        Bike.setImage("images/bike.png");
        Habitat habitat = new Habitat(1500, 700, 4000);
    }
}