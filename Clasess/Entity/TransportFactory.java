package Clasess.Entity;

public class TransportFactory {
    public interface ITransportFactory {
        Transport createTransport(int x, int y);
    }

    public static class CarFactory implements ITransportFactory {
        @Override
        public Transport createTransport(int x, int y) {
            return new Car(x, y);
        }
    }

    public static class BikeFactory implements ITransportFactory {
        @Override
        public Transport createTransport(int x, int y) {
            return new Bike(x, y);
        }
    }
}