package src.view;

import src.entity.transport.Transport;

import javax.swing.*;
import java.awt.*;

public class TransportLabel extends JLabel
{
    public static final int WIDTH_IMAGE = 100, HEIGHT_IMAGE = 100;
    public Transport transport;

    public TransportLabel(Transport transport)
    {
        super();
        this.transport = transport;

        Image image = transport.getImage().getScaledInstance(WIDTH_IMAGE, HEIGHT_IMAGE, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(image));
        setBounds(transport.getX(), transport.getY(), WIDTH_IMAGE, HEIGHT_IMAGE);
    }

    public void setCoordinates(int x, int y) {
        transport.setX(x);
        transport.setY(y);

        setBounds(x, y, WIDTH_IMAGE, HEIGHT_IMAGE);
    }
}