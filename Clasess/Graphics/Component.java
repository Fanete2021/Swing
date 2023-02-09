package Clasess.Graphics;

import Clasess.Entity.Car;
import Clasess.Entity.Transport;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Component extends JComponent
{
    private Transport transport;
    private final int WIDTH = 100, HEIGHT = 100;

    public Component(Transport transport)
    {
        this.transport = transport;
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(transport.getImage(), transport.getX(), transport.getY(), WIDTH, HEIGHT, null);
        repaint();
    }
}