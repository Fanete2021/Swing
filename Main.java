import src.config.Configuration;
import src.config.Keys;
import src.core.Habitat;
import src.entity.transport.Bike;
import src.entity.transport.Car;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        initSetup(config);

        Habitat.createInstance(1200, 700, 5, config);
    }

    public static void initSetup(Configuration config) {
        Car.setImage("src/images/car.png");
        Bike.setImage("src/images/bike.png");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                config.save();
            }
        });

        Car.frequency = Float.parseFloat(config.getProperty(Keys.CHANCE_GENERATION_CAR));
        Car.generationTime = Float.parseFloat(config.getProperty(Keys.GENERATION_TIME_CAR));
        Car.isMoving = Boolean.parseBoolean(config.getProperty(Keys.IS_MOVING_CAR));

        Bike.frequency = Float.parseFloat(config.getProperty(Keys.CHANCE_GENERATION_BIKE));
        Bike.generationTime = Float.parseFloat(config.getProperty(Keys.GENERATION_TIME_BIKE));
        Bike.isMoving = Boolean.parseBoolean(config.getProperty(Keys.IS_MOVING_BIKE));
    }
}