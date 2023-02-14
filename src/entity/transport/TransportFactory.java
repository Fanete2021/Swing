package src.entity.transport;

public class TransportFactory {
    public interface ITransportFactory {
        Transport createTransport(int x, int y, float timeBirth, float lifetime);
        float getFrequency();
        float getGenerationTime();
    }

    public static class CarFactory implements ITransportFactory {
        @Override
        public Transport createTransport(int x, int y, float timeBirth, float lifetime) {
            return new Car(x, y, timeBirth, lifetime);
        }

        @Override
        public float getFrequency() {
            return Car.frequency;
        }

        @Override
        public float getGenerationTime() {
            return Car.generationTime;
        }
    }

    public static class BikeFactory implements ITransportFactory {
        @Override
        public Transport createTransport(int x, int y, float timeBirth, float lifetime) {
            return new Bike(x, y, timeBirth, lifetime);
        }

        @Override
        public float getFrequency() {
            return Bike.frequency;
        }

        @Override
        public float getGenerationTime() {
            return Bike.generationTime;
        }
    }
}