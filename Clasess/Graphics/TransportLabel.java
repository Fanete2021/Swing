package Clasess.Graphics;

import Clasess.Entity.Transport;

import javax.swing.*;
import java.awt.*;

public class TransportLabel extends JLabel
{
    private final int WIDTH = 100, HEIGHT = 100;
    public Transport transport;

    public TransportLabel(Transport transport)
    {
        super();
        this.transport = transport;

        Image image = transport.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(image));
        setBounds(transport.getX(), transport.getY(), WIDTH, HEIGHT);
    }

    public void setCoordinates(int x, int y) {
        transport.setX(x);
        transport.setY(y);

        setBounds(x, y, WIDTH, HEIGHT);
    }
}