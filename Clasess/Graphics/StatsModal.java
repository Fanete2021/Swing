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
    private JTextArea statistics;
    private Emitter emitter;

    public StatsModal(JFrame parent, String title, String text, Emitter emitter) {
        super(parent, title, true);
        this.emitter = emitter;
        setLayout(null);
        Font customFont = Font.getFont(SERIF);

        try {
            InputStream is = getClass().getResourceAsStream("/fonts/Old-Soviet.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

        statistics = new JTextArea(text);
        statistics.setBounds(10, 10, 400, 290);
        statistics.setEditable(false);
        statistics.setFont(customFont);
        statistics.setBackground(null);
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
