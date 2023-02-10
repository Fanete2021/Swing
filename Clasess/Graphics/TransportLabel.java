package Clasess.Graphics;

import Clasess.Entity.Transport;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

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
}