package Clasess.Core;

import Clasess.Emitter.Emitter;
import Clasess.Graphics.ControlPanel;
import Clasess.Graphics.MenuBar;
import Clasess.Graphics.TransportLabel;
import Clasess.Graphics.TransportsPanel;

import javax.swing.*;
import java.awt.*;

public class Screen extends JFrame {
    private final Clasess.Graphics.MenuBar menu;
    private final TransportsPanel transports;
    private final ControlPanel control;
    private int width, height;

    public Screen(int width, int height, Emitter emitter) {
        super("Транспортные средства");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.width = width;
        this.height = height;

        menu = new MenuBar(emitter);
        setJMenuBar(menu);

        int transportsHeight = getHeightTransportsPanel();
        int transportsWidth = getWidthTransportsPanel();
        transports = new TransportsPanel(transportsWidth, transportsHeight);

        float coefWidthControl = 1 / 3f;
        int controlWidth = (int)(width * coefWidthControl);
        control = new ControlPanel(controlWidth, height, emitter);

        add(transports, BorderLayout.WEST);
        add(control, BorderLayout.EAST);

        setVisible(true);
    }

    public void removeTransports() {
        transports.removeTransports();
        repaint();
    }

    public void addToTransportsPanel(TransportLabel transport) {
        transports.addTransport(transport);
        repaint();
    }

    public void removeTransport(TransportLabel transport) {
        transports.removeTransport(transport);
        repaint();
    }

    public int getWidthTransportsPanel() {
        float coefWidthTransports = 2 / 3f;
        return (int)(width * coefWidthTransports);
    }

    public int getHeightTransportsPanel() {
        return height;
    }
}
