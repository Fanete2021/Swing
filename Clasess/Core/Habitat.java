package Clasess.Core;

import Clasess.Emitter.ActionControl;
import Clasess.Emitter.Actions;
import Clasess.Emitter.Emitter;
import Clasess.Entity.Bike;
import Clasess.Entity.Car;
import Clasess.Entity.Transport;
import Clasess.Entity.TransportFactory;
import Clasess.Graphics.StatsModal;
import Clasess.Graphics.TransportLabel;
import Clasess.Graphics.Screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class Habitat {
    private final List<Transport> transports;
    private final Screen screen;
    private Timer timer;
    private long simulationTime, time;
    private boolean isVisibleTime, isVisibleStats;
    private final Emitter emitter;

    public Habitat(int width, int height, long time) {
        emitter = new Emitter();
        emitter.subscribe("Screen:Control", this::triggerAction);
        emitter.subscribe("Screen:Menu", this::triggerAction);
        screen = new Screen(width, height, emitter);
        settingKeys();
        isVisibleTime = false;
        isVisibleStats = false;

        transports = new ArrayList<>();
        simulationTime = 0L;
        this.time = time;
    }

    private void triggerAction(ActionControl actionControl) {
        switch (actionControl.action) {
            case START:
                init();
                break;
            case STOP:
                destroySimulation();
                break;
            case HIDE_TIME:
                isVisibleTime = false;
                break;
            case SHOW_TIME:
                isVisibleTime = true;
                break;
            case SHOW_STATS:
                isVisibleStats = true;
                break;
            case HIDE_STATS:
                isVisibleStats = false;
                break;
            case MODAL_CANCEL:
                timer.cancel();
                break;
            default: break;
        }
    }

    public void settingKeys() {
        KeyEventDispatcher dispatcher = new KeyEventDispatcher(){
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_B && simulationTime == 0L)
                    {
                        ActionControl action = new ActionControl(Actions.START);
                        emitter.emit("Habitat", action);
                        init();
                    } else if (e.getKeyCode() == KeyEvent.VK_E && simulationTime != 0L)
                    {
                        ActionControl action = new ActionControl(Actions.STOP);
                        emitter.emit("Habitat", action);
                        destroySimulation();
                    } else if (e.getKeyCode() == KeyEvent.VK_T) {
                        Actions action = isVisibleTime ? Actions.HIDE_TIME : Actions.SHOW_TIME;
                        ActionControl actionControl = new ActionControl(action);
                        emitter.emit("Habitat", actionControl);
                        isVisibleTime = !isVisibleTime;
                    }
                }
                return false;
            }
        };

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(dispatcher);
    }

    private void init() {
        timer = new Timer();
        simulationTime = 0L;

        long period = 100;
        long delay = 100;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                simulationTime += period;
                updateScreen(simulationTime);
            }
        }, 0, period);
    }

    private void updateScreen(long currentTime) {
        ActionControl action = new ActionControl(Actions.UPDATE_TIME, (int)currentTime);
        emitter.emit("Habitat", action);

        int[] generationTimes = {Car.generationTime, Bike.generationTime};
        float[] frequencies = {Car.frequency, Bike.frequency};
        TransportFactory.ITransportFactory[] transportClasses = {
                new TransportFactory.CarFactory(),
                new TransportFactory.BikeFactory()
        };
        int width = screen.getWidthTransportsPanel();
        int height = screen.getHeightTransportsPanel();
        int minX = 150, minY = 150;

        for (int i = 0; i < generationTimes.length; i++) {
            if (currentTime != 0 && currentTime % generationTimes[i] == 0 && Utils.checkChance(frequencies[i])) {
                int x = Utils.generateInteger(width - 2 * minX, minX);
                int y = Utils.generateInteger(height - 2 * minY, minY);

                transports.add(transportClasses[i].createTransport(x, y));
                TransportLabel component = new TransportLabel(transports.get(transports.size() - 1));
                screen.addToTransportsPanel(component);
            }
        }

        if (currentTime == time) {
            stopSimulation();
        }
    }

    private void stopSimulation() {
        ActionControl actionControl = new ActionControl(Actions.SHOW_TIME);
        emitter.emit("Habitat", actionControl);

        if (isVisibleStats) {
            StatsModal modalView = new StatsModal(screen, "Статистика", createStatistics(), emitter);
        } else {
            timer.cancel();
        }
    }

    private String createStatistics() {
        int seconds = (int)(simulationTime / 1000);
        int ms = (int)(simulationTime % 1000);
        String[] stats = {
                "Прошло секунд: " + seconds + "." + ms / 100 + "с",
                "Машин создано: " + Car.count,
                "Мотоциклов создано: " + Bike.count
        };

        String result = "";
        for (int i = 0; i < stats.length; ++i) {
            result += stats[i] + "\n";
        }

        return result;
    }

    private void destroySimulation() {
        ActionControl action = new ActionControl(Actions.UPDATE_TIME, 0);
        emitter.emit("Habitat", action);
        timer.cancel();
        transports.clear();
        Bike.count = 0;
        Car.count = 0;
        simulationTime = 0L;
        screen.removeTransports();
    }
}
