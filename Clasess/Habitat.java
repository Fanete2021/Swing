package Clasess;

import Clasess.Entity.Bike;
import Clasess.Entity.Car;
import Clasess.Entity.Transport;
import Clasess.Entity.TransportFactory;
import Clasess.Graphics.Component;
import Clasess.Graphics.Screen;
import Clasess.Graphics.Statistics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class Habitat {
    private List<Transport> transports;
    private final Screen screen;
    private final int width, height;
    private Timer timer;
    private long simulationTime, time;
    private Statistics statistics;
    private boolean isVisibleStatistics;

    public Habitat(int width, int height, long time) {
        this.width = width;
        this.height = height;
        screen = new Screen(width, height);
        settingKeys();
        statistics = new Statistics();
        isVisibleStatistics = false;

        transports =  new ArrayList<Transport>();
        simulationTime = 0L;
        this.time = time;
    }

    public void settingKeys() {
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_B && simulationTime == 0L)
                {
                    init();
                } else if (e.getKeyCode() == KeyEvent.VK_E && timer != null)
                {
                    destroySimulation();
                } else if (e.getKeyCode() == KeyEvent.VK_T) {
                    if (isVisibleStatistics) {
                        screen.deleteComponent(statistics);
                    } else {
                        screen.addComponent(statistics);
                    }

                    isVisibleStatistics = !isVisibleStatistics;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        };

        screen.addKeyListener(keyListener);
    }

    private void init() {
        timer = new Timer();
        simulationTime = 0L;

        long period = 100;
        long delay = 100;
        simulationTime += delay;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                simulationTime += period;
                updateScreen(simulationTime);
            }
        }, 0, period);
    }

    private void updateScreen(long currentTime) {
        int[] generationTimes = {Car.generationTime, Bike.generationTime};
        float[] frequencies = {Car.frequency, Bike.frequency};
        TransportFactory.ITransportFactory[] transportClasses = {
                new TransportFactory.CarFactory(),
                new TransportFactory.BikeFactory()
        };
        int minX = 100, minY = 100;

        for (int i = 0; i < generationTimes.length; i++) {
            if (currentTime != 0 && currentTime % generationTimes[i] == 0 && Utils.checkChance(frequencies[i])) {
                int x = Utils.generateInteger(width - 2 * minX, minX);
                int y = Utils.generateInteger(height - 2 * minY, minY);

                transports.add(transportClasses[i].createTransport(x, y));
                Component component = new Component(transports.get(transports.size() - 1));
                screen.addComponent(component);
            }
        }

        if (currentTime % 1000 == 0) {
            updateStatistics();
        }

        if (currentTime == time) {
            stopSimulation();
        }
    }

    private void updateStatistics() {
        statistics.clearStats();
        String[] stats = {
                "Прошло секунд: " + simulationTime / 1000,
                "Машин создано: " + Car.count,
                "Мотоциклов создано: " + Bike.count
        };

        for (int i = 0; i < stats.length; ++i) {
            statistics.addStats(stats[i]);
        }
    }

    private void stopSimulation() {
        if (!isVisibleStatistics) {
            screen.addComponent(statistics);
        }
        timer.cancel();
    }

    private void destroySimulation() {
        timer.cancel();
        transports = new ArrayList<Transport>();
        statistics.clearStats();
        Bike.count = 0;
        Car.count = 0;
        simulationTime = 0L;
        screen.clear();
    }
}
