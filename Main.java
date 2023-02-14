import src.config.Configuration;
import src.config.Keys;
import src.entity.transport.Bike;
import src.entity.transport.Car;
import src.core.Habitat;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        initSetup(config);

        Habitat.createInstance(1200, 700, 50, config);
    }

    public static void initSetup(Configuration config) {
        Car.setImage("src/resources/car.png");
        Bike.setImage("src/resources/bike.png");

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