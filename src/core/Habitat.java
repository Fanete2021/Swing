package src.core;

import src.core.Emitter.ActionControl;
import src.core.Emitter.Actions;
import src.core.Emitter.Emitter;
import src.core.Emitter.Events;
import src.entity.ai.CarAI;
import src.entity.transport.Bike;
import src.entity.ai.BikeAI;
import src.entity.transport.Car;
import src.entity.transport.Transport;
import src.entity.transport.TransportFactory;
import src.view.CurrentObjectModal;
import src.view.Screen;
import src.view.StatsModal;
import src.view.TransportLabel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class Habitat {
    private static Habitat instance = null;

    private List<TransportLabel> transportList;
    private HashSet<Integer> transportIds;
    private TreeMap<Integer, Float> transportLifetime;
    private Screen screen;
    private long time, periodTimer;
    private float lifetimeCar, lifetimeBike;
    private boolean isVisibleTime, isVisibleStats;
    private Emitter emitter;
    private CurrentObjectModal objectModal;
    private CarAI carAI;
    private BikeAI bikeAI;
    private Updater updater;

    private Habitat(int width, int height, float time) {
        if (instance != null) {
            lifetimeCar = 5f;
            lifetimeBike = 3f;
            periodTimer = 100;
            updater = new Updater(periodTimer, this::updateScreen);
            this.time = Utils.convertMinutesToMs(time);

            transportList = Collections.synchronizedList(new ArrayList<>());
            transportIds = new HashSet<>();
            transportLifetime = new TreeMap<>();
            objectModal = new CurrentObjectModal("Текущие объекты", createInfoObjects());

            emitter = new Emitter();
            emitter.subscribe(Events.CONTROL, this::triggerAction);
            emitter.subscribe(Events.MENU, this::triggerAction);
            emitter.subscribe(Events.MODAL, this::triggerAction);

            screen = new Screen(width, height, emitter);
            settingKeys();
            isVisibleTime = true;
            isVisibleStats = true;

            carAI = new CarAI(transportList, screen.getWidthTransportsPanel());
            bikeAI = new BikeAI(transportList, screen.getHeightTransportsPanel());
            carAI.start();
            bikeAI.start();
        }
    }

    public static void createInstance(int width, int height, float time) {
        instance = new Habitat(width, height, time);
    }

    public static Habitat getInstance() {
        return instance;
    }

    private void triggerAction(ActionControl actionControl) {
        switch (actionControl.action) {
            case START:
                start();
                break;
            case CONTINUE:
                continueSimulation();
                break;
            case IS_SHOW_TIME:
                isVisibleTime = (boolean)actionControl.state;
                break;
            case IS_SHOW_STATS:
                isVisibleStats = (boolean)actionControl.state;
                break;
            case CLEAR:
                clear();
                break;
            case CHANGE_LIFETIME_CAR:
                lifetimeCar = (float)actionControl.state;
                break;
            case CHANGE_LIFETIME_BIKE:
                lifetimeBike = (float)actionControl.state;
                break;
            case SHOW_CURRENT_TRANSPORT:
                objectModal.setVisible(true);
                break;
            case IS_MOVEMENT_CAR:
                Car.isMoving = (boolean)actionControl.state;
                if (Car.isMoving) {
                    synchronized (carAI.lock) {
                        carAI.lock.notify();
                    }
                }
                break;
            case IS_MOVEMENT_BIKE:
                Bike.isMoving = (boolean)actionControl.state;
                if (Bike.isMoving) {
                    synchronized (bikeAI.lock) {
                        bikeAI.lock.notify();
                    }
                }
                break;
            case CHANGE_PRIORITY_THREAD_BIKE:
                bikeAI.setPriority((int)actionControl.state);
                break;
            case CHANGE_PRIORITY_THREAD_CAR:
                carAI.setPriority((int)actionControl.state);
                break;
            default: break;
        }
    }

    private void settingKeys() {
        KeyEventDispatcher dispatcher = new KeyEventDispatcher(){
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_B && updater.getTime() == 0L)
                    {
                        emitter.notify(Events.HABITAT, Actions.START);
                        start();
                    } else if (e.getKeyCode() == KeyEvent.VK_E && updater.getTime() != 0L)
                    {
                        emitter.notify(Events.HABITAT, Actions.CLEAR);
                        clear();
                    } else if (e.getKeyCode() == KeyEvent.VK_T) {
                        isVisibleTime = !isVisibleTime;
                        emitter.notify(Events.HABITAT, Actions.IS_SHOW_TIME, isVisibleTime);
                    }
                }
                return false;
            }
        };

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(dispatcher);
    }

    public void start() {
        startThreads();
        updater.start();
    }

    public void stopSimulation() {
        updater.stop();
        carAI.isWorking = false;
        bikeAI.isWorking = false;
    }

    public void continueSimulation() {
        updater.start();
        startThreads();
    }

    private void updateScreen(long currentTime) {
        float minutes = Utils.convertMsToMinutes(updater.getTime());
        emitter.notify(Events.HABITAT, Actions.UPDATE_TIME, minutes);

        checkLifetimeAndDelete();
        spawnTransports();

        if (currentTime == time) {
            stopSimulation();

            if (isVisibleStats) {
                String stats = Utils.joinArray(createStatistics());
                StatsModal modalView = new StatsModal(screen, "Статистика", stats, emitter);
            }
        }
    }

    private void checkLifetimeAndDelete() {
        float minutes = Utils.convertMsToMinutes(updater.getTime());

        for (int i = 0; i < transportList.size(); i++) {
            TransportLabel canvas = transportList.get(i);
            Transport transport = canvas.transport;

            if (transport.isDead(minutes)) {
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
        float[] lifetimes = {
                lifetimeCar,
                lifetimeBike
        };
        TransportFactory.ITransportFactory[] transportClasses = {
                new TransportFactory.CarFactory(),
                new TransportFactory.BikeFactory()
        };
        int width = screen.getWidthTransportsPanel();
        int height = screen.getHeightTransportsPanel();

        float minutes = Utils.convertMsToMinutes(updater.getTime());

        for (int i = 0; i < transportClasses.length; i++) {
            long ms = Utils.convertMinutesToMs(transportClasses[i].getGenerationTime());

            if (updater.getTime() % ms == 0 && Utils.checkChance(transportClasses[i].getFrequency())) {

                int x = Utils.generateInteger(width - TransportLabel.WIDTH_IMAGE);
                int y = Utils.generateInteger((int)(height - 1.5 * TransportLabel.HEIGHT_IMAGE));

                Transport transport = transportClasses[i].createTransport(x, y, minutes, lifetimes[i]);
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

    private void startThreads() {
        if (!carAI.isWorking) {
            carAI.isWorking = true;
            synchronized (carAI.lock) {
                carAI.lock.notify();
            }
        }

        if (!bikeAI.isWorking) {
            bikeAI.isWorking = true;
            synchronized (bikeAI.lock) {
                bikeAI.lock.notify();
            }
        }
    }

    private String[] createStatistics() {
        String[] stats = {
                "Прошло секунд: " + Utils.toStringTime(updater.getTime()) + "с",
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
                    ", Born time " + transport.getTimeBirth() + "c" + "\n";
        }

        return infoObjects;
    }

    public void clear() {
        updater.clear();
        transportList.clear();
        transportIds.clear();
        transportLifetime.clear();
        Bike.count = 0;
        Car.count = 0;
        screen.removeTransports();
    }
}
