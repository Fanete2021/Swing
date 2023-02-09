package Clasess.Graphics;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Button extends JButton {
    public Button(String text, int x, int y, ActionListener actionListener) {
        super(text);
        setBounds(x, y, 100, 50);
        setFocusPainted(false);
        setContentAreaFilled(false);
        addActionListener(actionListener);
    }
}
