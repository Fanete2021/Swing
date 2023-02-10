package Clasess.Graphics;

import Clasess.Emitter.ActionControl;
import Clasess.Emitter.Actions;
import Clasess.Emitter.Emitter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import static java.awt.Font.SERIF;

public class StatsModal extends JDialog {
    private final Button ok, cancel;
    private TextArea statistics;
    private Emitter emitter;

    public StatsModal(JFrame parent, String title, String stats, Emitter emitter) {
        super(parent, title, true);
        this.emitter = emitter;
        setLayout(null);

        statistics = new TextArea(stats, 24f);
        statistics.setBounds(10, 10, 400, 290);

        ok = new Button("ОК", 50, 300, getOkActionListener());
        cancel = new Button("Отмена", 250, 300, getCancelActionListener());

        add(statistics);
        add(ok);
        add(cancel);

        setSize(400, 400);
        setVisible(true);
    }

    private ActionListener getCancelActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionControl action = new ActionControl(Actions.MODAL_CANCEL);
                emitter.emit("Screen:Control", action);
                destroy();
            }
        };
    }

    private ActionListener getOkActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                destroy();
            }
        };
    }

    private void destroy() {
        setVisible(false);
        dispose();
    }
}
