package src.view;

import src.config.Configuration;
import src.config.Keys;
import src.core.Emitter.ActionControl;
import src.core.Emitter.Actions;
import src.core.Emitter.Emitter;
import src.core.Emitter.Events;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    private Emitter emitter;
    private Configuration config;
    private JMenuItem start, clear, call, save, load;
    private JCheckBoxMenuItem time, stats;
    private Console console;

    public MenuBar(Emitter emitter, Configuration config) {
        this.emitter = emitter;
        emitter.subscribe(Events.CONTROL, this::triggerAction);
        emitter.subscribe(Events.HABITAT, this::triggerAction);
        emitter.subscribe(Events.MODAL, this::triggerAction);

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

        console = new Console(emitter);

        JMenu consoleMenu = new JMenu("Консоль");

        call = new JMenuItem("Вызвать");
        call.addActionListener(e -> console.setVisible(true));

        JMenu file = new JMenu("Файл");

        save = new JMenuItem("Сохранить");
        save.addActionListener(e -> emitter.notify(Events.MENU, Actions.SAVE));

        load = new JMenuItem("Загрузить");
        load.addActionListener(e -> emitter.notify(Events.MENU, Actions.LOAD));

        actions.add(start);
        actions.add(clear);
        actions.add(time);
        actions.add(stats);

        consoleMenu.add(call);

        file.add(save);
        file.add(load);

        add(actions);
        add(consoleMenu);
        add(file);
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
