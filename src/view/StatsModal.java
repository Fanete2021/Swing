package src.view;

import src.core.Emitter.ActionControl;
import src.core.Emitter.Actions;
import src.core.Emitter.Emitter;
import src.core.Emitter.Events;

import javax.swing.*;

public class StatsModal extends JDialog {
    private final JButton contin, clear;
    private JTextArea statistics;
    private Emitter emitter;

    public StatsModal(JFrame parent, String title, String stats, Emitter emitter) {
        super(parent, title, true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.emitter = emitter;
        setLayout(null);

        statistics = FactoryView.createTextArea(stats, 24f);
        statistics.setBounds(10, 10, 400, 290);

        contin = FactoryView.createButton("Продолжить", 50, 300);
        contin.setBounds(50, 300, 150, 50);
        contin.addActionListener(e -> {
            emitter.notify(Events.MODAL, Actions.CONTINUE);
            destroy();
        });

        clear = FactoryView.createButton("Очистить", 250, 300);
        clear.addActionListener(e -> {
            emitter.notify(Events.MODAL, Actions.CLEAR);
            destroy();
        });

        add(statistics);
        add(contin);
        add(clear);

        setSize(400, 400);
        setVisible(true);
    }

    private void destroy() {
        setVisible(false);
        dispose();
    }
}
