package Clasess.Graphics;

import javax.swing.*;

public class Screen extends JFrame {
    public Screen(int width, int height) {
        super("Транспортные средства");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void addComponent(JComponent component)
    {
        add(component);
        setVisible(true);
    }

    public void deleteComponent(JComponent component)
    {
        remove(component);
        repaint();
    }

    public void clear() {
        getContentPane().removeAll();
        revalidate();
        repaint();
    }
}
