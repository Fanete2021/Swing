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

public class CurrentObjectModal extends JDialog {
    private final Button ok;
    private final TextArea statistics;

    public CurrentObjectModal(JFrame parent, String title, String stats) {
        super(parent, title, true);
        setLayout(null);

        statistics = new TextArea(stats, 11f);

        ok = new Button("Закрыть", 110, 260, getOkActionListener());

        JScrollPane scroll = new JScrollPane(statistics);
        scroll.setBounds(10, 10, 290, 240);
        add(scroll);
        add(ok);

        setSize(320, 350);
    }

    private ActionListener getOkActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        };
    }

    public void updateText(String text) {
        statistics.setText(text);
        statistics.repaint();
    }
}
