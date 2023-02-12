package src.entity.ai;

import src.entity.transport.Transport;
import src.entity.transport.Car;
import src.view.TransportLabel;
import java.util.List;

public class CarAI extends BaseAI {
    private final int width;

    public CarAI(List<TransportLabel> list, int width) {
        super(list);
        this.width = width;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (super.lock) {
                if (!Car.isMoving || !super.isWorking) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < super.transports.size(); i++) {
                    Transport transport = super.transports.get(i).transport;
                    if (transport instanceof Car) {
                        move(super.transports.get(i));
                    }
                }

                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void move(TransportLabel label) {
        Transport transport = label.transport;
        int x = transport.getX();
        int y = transport.getY();
        int speed = transport.getSpeed();
        int newX = x + speed;

        if ((speed < 0 && 0 <= newX) || (speed > 0 && newX <= width - TransportLabel.WIDTH_IMAGE)) {
            transport.setX(newX);
            label.setCoordinates(newX, y);
        } else {
            speed = -speed;
            transport.setSpeed(speed);
            label.setCoordinates(x + speed, y);
        }
    }
}
