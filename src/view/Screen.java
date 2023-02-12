package src.view;

import src.core.Emitter.Emitter;

import javax.swing.*;
import java.awt.*;

public class Screen extends JFrame {
    private final src.view.MenuBar menu;
    private final TransportsPanel transports;
    private final ControlPanel control;

    public Screen(int width, int height, Emitter emitter) {
        super("Транспортные средства");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        menu = new MenuBar(emitter);
        setJMenuBar(menu);

        int transportsWidth = getWidthTransportsPanel();
        transports = new TransportsPanel(transportsWidth, height);

        float coefWidthControl = 1 / 4f;
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
        float coefWidthTransports = 3 / 4f;
        return (int)(getWidth() * coefWidthTransports);
    }

    public int getHeightTransportsPanel() {
        return getHeight();
    }
}
