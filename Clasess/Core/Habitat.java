package Clasess.Core;

import Clasess.Emitter.ActionControl;
import Clasess.Emitter.Actions;
import Clasess.Emitter.Emitter;
import Clasess.Emitter.Events;
import Clasess.Entity.*;
import Clasess.Graphics.CurrentObjectModal;
import Clasess.Graphics.StatsModal;
import Clasess.Graphics.TransportLabel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class Habitat {
    private final List<TransportLabel> transportList;
    private final HashSet<Integer> transportIds;
    private final TreeMap<Integer, Integer> transportLifetime;
    private final Screen screen;
    private Timer timer;
    private long simulationTime, time;
    private boolean isVisibleTime, isVisibleStats;
    private final Emitter emitter;
    private int lifetimeCar, lifetimeBike;
    private final CurrentObjectModal objectModal;
    private CarAI carAI;
    private BikeAI bikeAI;

    public Habitat(int width, int height, long time) {
        simulationTime = 0L;
        lifetimeCar = 5000;
        lifetimeBike = 3000;
        this.time = time;

        transportList = Collections.synchronizedList(new ArrayList<>());
        transportIds = new HashSet<>();
        transportLifetime = new TreeMap<>();
        objectModal = new CurrentObjectModal("Текущие объекты", createInfoObjects());

        emitter = new Emitter();
        emitter.subscribe(Events.CONTROL.getTitle(), this::triggerAction);
        emitter.subscribe(Events.MENU.getTitle(), this::triggerAction);
        screen = new Screen(width, height, emitter);
        settingKeys();
        isVisibleTime = false;
        isVisibleStats = false;

        carAI = new CarAI(transportList, screen.getWidthTransportsPanel());
        bikeAI = new BikeAI(transportList, screen.getHeightTransportsPanel());
        carAI.start();
        bikeAI.start();
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
            case CHANGE_LIFETIME_CAR:
                lifetimeCar = actionControl.state;
                break;
            case CHANGE_LIFETIME_BIKE:
                lifetimeBike = actionControl.state;
                break;
            case SHOW_CURRENT_TRANSPORT:
                objectModal.setVisible(true);
                break;
            case MOVEMENT_CAR_TRUE:
                Car.isMoving = true;
                synchronized (carAI.lock) {
                    carAI.lock.notify();
                }
                break;
            case MOVEMENT_CAR_FALSE:
                Car.isMoving = false;
                break;
            case MOVEMENT_BIKE_TRUE:
                Bike.isMoving = true;
                synchronized (bikeAI.lock) {
                    bikeAI.lock.notify();
                }
                break;
            case MOVEMENT_BIKE_FALSE:
                Bike.isMoving = false;
                break;
            case CHANGE_PRIORITY_THREAD_BIKE:
                bikeAI.setPriority(actionControl.state);
                break;
            case CHANGE_PRIORITY_THREAD_CAR:
                carAI.setPriority(actionControl.state);
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
                        emitter.emit(Events.HABITAT.getTitle(), action);
                        init();
                    } else if (e.getKeyCode() == KeyEvent.VK_E && simulationTime != 0L)
                    {
                        ActionControl action = new ActionControl(Actions.STOP);
                        emitter.emit(Events.HABITAT.getTitle(), action);
                        destroySimulation();
                    } else if (e.getKeyCode() == KeyEvent.VK_T) {
                        Actions action = isVisibleTime ? Actions.HIDE_TIME : Actions.SHOW_TIME;
                        ActionControl actionControl = new ActionControl(action);
                        emitter.emit(Events.HABITAT.getTitle(), actionControl);
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

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ActionControl action = new ActionControl(Actions.UPDATE_TIME, (int)simulationTime);
                emitter.emit(Events.HABITAT.getTitle(), action);

                updateScreen(simulationTime);
                simulationTime += period;
            }
        }, 0, period);
    }

    private void updateScreen(long currentTime) {
        checkLifetimeAndDelete();
        spawnTransports();

        if (currentTime == time) {
            stopSimulation();
            System.out.println(currentTime + " " + time);
        }
    }

    private void checkLifetimeAndDelete() {
        for (int i = 0; i < transportList.size(); i++) {
            TransportLabel canvas = transportList.get(i);
            Transport transport = canvas.transport;
            int time = transport.getTimeBirth() + transport.getLifetime();

            if (time <= simulationTime) {
                transportLifetime.remove(transport.getId());
                transportIds.remove(transport.getId());
                screen.removeTransport(canvas);
                transportList.remove(i);
                i--;
                objectModal.updateText(createInfoObjects());
            }
        }
    }

    private void spawnTransports() {
        int[] lifetimes = {
                lifetimeCar,
                lifetimeBike
        };
        TransportFactory.ITransportFactory[] transportClasses = {
                new TransportFactory.CarFactory(),
                new TransportFactory.BikeFactory()
        };
        int width = screen.getWidthTransportsPanel();
        int height = screen.getHeightTransportsPanel();
        int minX = 150, minY = 150;

        for (int i = 0; i < transportClasses.length; i++) {
            if (simulationTime != 0 && simulationTime % transportClasses[i].getGenerationTime() == 0
                    && Utils.checkChance(transportClasses[i].getFrequency())) {

                int x = Utils.generateInteger(width - 2 * minX, minX);
                int y = Utils.generateInteger(height - 2 * minY, minY);

                Transport transport = transportClasses[i].createTransport(x, y, (int)simulationTime, lifetimes[i]);
                createTransportLabelAndAddToScreen(transport);
                objectModal.updateText(createInfoObjects());
            }
        }
    }

    private void createTransportLabelAndAddToScreen(Transport transport) {
        TransportLabel component = new TransportLabel(transport);
        transportList.add(component);
        transportIds.add(transport.getId());
        transportLifetime.put(transport.getId(), transport.getTimeBirth());

        screen.addToTransportsPanel(component);
    }

    private void stopSimulation() {
        if (isVisibleStats) {
            String stats = Utils.joinArray(createStatistics());
            StatsModal modalView = new StatsModal(screen, "Статистика", stats, emitter);
        } else {
            timer.cancel();
        }
    }

    private String[] createStatistics() {
        String[] stats = {
                "Прошло секунд: " + Utils.toStringTime((int)simulationTime) + "с",
                "Машин создано: " + Car.count,
                "Мотоциклов создано: " + Bike.count
        };

        return stats;
    }

    private String createInfoObjects() {
        String infoObjects = transportList.size() > 0 ? "" : "Список пуст";

        for (int i = 0; i < transportList.size(); i++) {
            TransportLabel label = transportList.get(i);
            Transport transport = label.transport;
            int id = transport.getId();

            infoObjects += "ID: " + id +
                    ", Type: " + transport.getClass().getSimpleName() +
                    ", Born time " + transport.getTimeBirth() + "\n";
        }

        return infoObjects;
    }

    private void destroySimulation() {
        ActionControl action = new ActionControl(Actions.UPDATE_TIME, 0);
        emitter.emit(Events.HABITAT.getTitle(), action);
        timer.cancel();
        transportList.clear();
        transportIds.clear();
        transportLifetime.clear();
        Bike.count = 0;
        Car.count = 0;
        simulationTime = 0L;
        screen.removeTransports();
    }
}
