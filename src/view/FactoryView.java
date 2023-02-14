package src.view;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Vector;

import static java.awt.Font.SERIF;

public class FactoryView {
    public static JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 100, 50);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        return button;
    }

    public static JTextArea createTextArea(String text, float sizeFont) {
        JTextArea textArea = new JTextArea(text);

        Font customFont = Font.getFont(SERIF);
        try {
            InputStream is = FactoryView.class.getResourceAsStream("/src/resources/Old-Soviet.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(sizeFont);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

        textArea.setEditable(false);
        textArea.setFont(customFont);
        textArea.setBackground(null);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        return textArea;
    }

    public static JRadioButton createRadioButton(ButtonGroup bg, String text, int x, int y) {
        JRadioButton radioButton = new JRadioButton(text);

        radioButton.setBounds(x, y, 150, 20);
        radioButton.setFocusPainted(false);
        radioButton.setContentAreaFilled(false);
        bg.add(radioButton);

        return radioButton;
    }

    public static JCheckBox createCheckBox(String text, boolean isSelected, int x, int y) {
        JCheckBox checkBox = new JCheckBox(text);

        checkBox.setSelected(isSelected);
        checkBox.setBounds(x, y, 200, 20);
        checkBox.setFocusPainted(false);

        return checkBox;
    }

    public static JComboBox createComboBox(Vector<String> data, int x, int y, int selectedIndex) {
        JComboBox comboBox = new JComboBox(data);

        comboBox.setMaximumRowCount(4);
        comboBox.setBounds(x, y, 70, 20);
        comboBox.setSelectedIndex(selectedIndex);

        return comboBox;
    }
}
