package Clasess.Entity;


import Clasess.Emitter.Emitter;

public class TransportFactory {
    public interface ITransportFactory {
        Transport createTransport(int x, int y, int timeBirth, int lifetime);
        float getFrequency();
        int getGenerationTime();
    }

    public static class CarFactory implements ITransportFactory {
        @Override
        public Transport createTransport(int x, int y, int timeBirth, int lifetime) {
            return new Car(x, y, timeBirth, lifetime);
        }

        @Override
        public float getFrequency() {
            return Car.frequency;
        }

        @Override
        public int getGenerationTime() {
            return Car.generationTime;
        }
    }

    public static class BikeFactory implements ITransportFactory {
        @Override
        public Transport createTransport(int x, int y, int timeBirth, int lifetime) {
            return new Bike(x, y, timeBirth, lifetime);
        }

        @Override
        public float getFrequency() {
            return Bike.frequency;
        }

        @Override
        public int getGenerationTime() {
            return Bike.generationTime;
        }
    }
}