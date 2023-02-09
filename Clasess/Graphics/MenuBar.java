package Clasess.Graphics;

import Clasess.Emitter.ActionControl;
import Clasess.Emitter.Actions;
import Clasess.Emitter.Emitter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar {
    private Emitter emitter;
    private JMenuItem start, stop;
    private JCheckBoxMenuItem time, stats;

    public MenuBar(Emitter emitter) {
        this.emitter = emitter;
        emitter.subscribe("Screen:Control", this::triggerAction);
        emitter.subscribe("Habitat", this::triggerAction);

        JMenu actions = new JMenu("Действия");

        start = new JMenuItem("Старт");
        start.addActionListener(getStartActionListener());

        stop = new JMenuItem("Стоп");
        stop.addActionListener(getStopActionListener());
        stop.setEnabled(false);

        time = new JCheckBoxMenuItem("Показать время");
        time.addActionListener(getTimeActionListener());

        stats = new JCheckBoxMenuItem("Показать статистику");
        stats.addActionListener(getStatsActionListener());

        actions.add(start);
        actions.add(stop);
        actions.add(time);
        actions.add(stats);
        add(actions);
    }

    private void triggerAction(ActionControl actionControl) {
        switch (actionControl.action) {
            case STOP:
            case START:
                switchButton();
                break;
            case HIDE_TIME:
                time.setSelected(false);
                break;
            case SHOW_TIME:
                time.setSelected(true);;
                break;
            case SHOW_STATS:
                stats.setSelected(true);
                break;
            case HIDE_STATS:
                stats.setSelected(false);
                break;
            default: break;
        }
    }

    private ActionListener getStartActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchButton();
                ActionControl action = new ActionControl(Actions.START);
                emitter.emit("Screen:Control", action);
                emitter.emit("Habitat", action);
            }
        };
    }

    private ActionListener getStopActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchButton();
                ActionControl action = new ActionControl(Actions.STOP);
                emitter.emit("Screen:Menu", action);
            }
        };
    }

    private ActionListener getTimeActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Actions action = time.isSelected() ? Actions.SHOW_TIME : Actions.HIDE_TIME;
                ActionControl actionControl = new ActionControl(action);
                emitter.emit("Screen:Menu", actionControl);
            }
        };
    }

    private ActionListener getStatsActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Actions action = stats.isSelected() ? Actions.SHOW_STATS : Actions.HIDE_STATS;
                ActionControl actionControl = new ActionControl(action);
                emitter.emit("Screen:Menu", actionControl);
            }
        };
    }

    private void switchButton() {
        if (start.isEnabled()) {
            start.setEnabled(false);
            stop.setEnabled(true);
        } else {
            start.setEnabled(true);
            stop.setEnabled(false);
        }
    }
}
