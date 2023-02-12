package src.view;

import src.core.Emitter.ActionControl;
import src.core.Emitter.Actions;
import src.core.Emitter.Emitter;
import src.core.Emitter.Events;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    private Emitter emitter;
    private JMenuItem start, clear;
    private JCheckBoxMenuItem time, stats;

    public MenuBar(Emitter emitter) {
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
        time.addActionListener(e -> emitter.notify(Events.MENU, Actions.IS_SHOW_TIME, time.isSelected()));
        time.setSelected(true);

        stats = new JCheckBoxMenuItem("Показать статистику");
        stats.addActionListener(e -> emitter.notify(Events.MENU, Actions.IS_SHOW_STATS, stats.isSelected()));
        stats.setSelected(true);

        actions.add(start);
        actions.add(clear);
        actions.add(time);
        actions.add(stats);
        add(actions);
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
