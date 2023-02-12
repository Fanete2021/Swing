import src.entity.transport.Bike;
import src.entity.transport.Car;
import src.core.Habitat;

public class Main {
    public static void main(String[] args) {
        Car.setImage("src/images/car.png");
        Bike.setImage("src/images/bike.png");

        Habitat.createInstance(1200, 700, 5);
    }
}