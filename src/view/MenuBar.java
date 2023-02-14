package src.view;

import src.config.Configuration;
import src.config.Keys;
import src.core.Emitter.ActionControl;
import src.core.Emitter.Actions;
import src.core.Emitter.Emitter;
import src.core.Emitter.Events;
import src.core.Habitat;
import src.data.SqlWorker;
import src.entity.transport.Transport;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MenuBar extends JMenuBar {
    private Emitter emitter;
    private Configuration config;
    private JMenuItem start, clear, call, saveFile, loadFile;
    private JMenuItem saveCars, saveBikes, loadCars, loadBikes;
    private JCheckBoxMenuItem time, stats;
    private Console console;

    public MenuBar(Emitter emitter, Configuration config) {
        this.emitter = emitter;
        emitter.subscribe(Events.CONTROL, this::triggerAction);
        emitter.subscribe(Events.HABITAT, this::triggerAction);
        emitter.subscribe(Events.MODAL, this::triggerAction);
        this.config = config;

        createActionsMenu();
        createConsoleMenu();
        createFileMenu();
        createDatabaseMenu();
    }

    private void triggerAction(ActionControl actionControl) {
        switch (actionControl.action) {
            case CLEAR:
            case START:
                switchButton();
                break;
            case IS_SHOW_TIME:
                time.setSelected((boolean) actionControl.state);
                break;
            case IS_SHOW_STATS:
                stats.setSelected((boolean)actionControl.state);
                break;
            default: break;
        }
    }

    private void createActionsMenu() {
        JMenu actions = new JMenu("Действия");

        start = new JMenuItem("Старт");
        start.addActionListener(e -> {
            switchButton();
            emitter.notify(Events.MENU, Actions.START);
        });

        clear = new JMenuItem("Очистить");
        clear.addActionListener(e -> {
            switchButton();
            emitter.notify(Events.MENU, Actions.CLEAR);
        });
        clear.setEnabled(false);

        time = new JCheckBoxMenuItem("Показать время");
        time.addActionListener(e -> {
            emitter.notify(Events.MENU, Actions.IS_SHOW_TIME, time.isSelected());
            config.setProperty(Keys.IS_SHOW_TIME, Boolean.toString(time.isSelected()));
        });
        time.setSelected(Boolean.parseBoolean(config.getProperty(Keys.IS_SHOW_TIME)));

        stats = new JCheckBoxMenuItem("Показать статистику");
        stats.addActionListener(e -> {
            emitter.notify(Events.MENU, Actions.IS_SHOW_STATS, stats.isSelected());
            config.setProperty(Keys.IS_SHOW_STATS, Boolean.toString(stats.isSelected()));
        });
        stats.setSelected(Boolean.parseBoolean(config.getProperty(Keys.IS_SHOW_STATS)));

        actions.add(start);
        actions.add(clear);
        actions.add(time);
        actions.add(stats);

        add(actions);
    }

    private void createConsoleMenu() {
        console = new Console(emitter);

        JMenu consoleMenu = new JMenu("Консоль");

        call = new JMenuItem("Вызвать");
        call.addActionListener(e -> console.setVisible(true));

        consoleMenu.add(call);

        add(consoleMenu);
    }

    private void createFileMenu() {
        JMenu file = new JMenu("Файл");

        saveFile = new JMenuItem("Сохранить");
        saveFile.addActionListener(e -> emitter.notify(Events.MENU, Actions.SAVE));

        loadFile = new JMenuItem("Загрузить");
        loadFile.addActionListener(e -> emitter.notify(Events.MENU, Actions.LOAD));


        file.add(saveFile);
        file.add(loadFile);

        add(file);
    }

    private void createDatabaseMenu() {
        JMenu database = new JMenu("База данных");
        JMenu save = new JMenu("Сохранить");
        JMenu load = new JMenu("Загрузить");

        saveCars = new JMenuItem("машины");
        saveCars.addActionListener(e -> {
            ArrayList<Transport> list = Habitat.getInstance().cloneTransports();;
            SqlWorker.save(list,"Car");
        });

        saveBikes = new JMenuItem("мотоциклы");
        saveBikes.addActionListener(e -> {
            ArrayList<Transport> list = Habitat.getInstance().cloneTransports();;
            SqlWorker.save(list,"Bike");
        });

        loadCars = new JMenuItem("машины");
        loadCars.addActionListener(e -> {
            ArrayList<Transport> list = SqlWorker.load("Car");
            Habitat.getInstance().loadTransports(list);
        });

        loadBikes = new JMenuItem("мотоциклы");
        loadBikes.addActionListener(e -> {
            ArrayList<Transport> list = SqlWorker.load("Bike");
            Habitat.getInstance().loadTransports(list);
        });

        save.add(saveCars);
        save.add(saveBikes);
        load.add(loadCars);
        load.add(loadBikes);

        database.add(save);
        database.add(load);

        add(database);
    }

    private void switchButton() {
        if (start.isEnabled()) {
            start.setEnabled(false);
            clear.setEnabled(true);
        } else {
            start.setEnabled(true);
            clear.setEnabled(false);
        }
    }
}
