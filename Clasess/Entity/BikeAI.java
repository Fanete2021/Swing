package Clasess.Entity;

import Clasess.Graphics.TransportLabel;

import java.util.List;

public class BikeAI extends BaseAI {
    private final int height;

    public BikeAI(List<TransportLabel> list, int height) {
        super(list);
        this.height = height;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (super.lock) {
                if (!Bike.isMoving) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < super.transports.size(); i++) {
                    Transport transport = super.transports.get(i).transport;
                    if (transport instanceof Bike) {
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
        int newY = y + speed;

        if ((speed < 0 && 0 <= newY) || (speed > 0 && newY <= height)) {
            transport.setY(newY);
            label.setCoordinates(x, newY);
        } else {
            speed = -speed;
            transport.setSpeed(speed);
            label.setCoordinates(x, y + speed);
        }
    }
}
