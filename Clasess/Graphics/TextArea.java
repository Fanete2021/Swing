package Clasess.Graphics;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

import static java.awt.Font.SERIF;

public class TextArea extends JTextArea {
    public TextArea(String text, float sizeFont) {
        super(text);

        Font customFont = Font.getFont(SERIF);

        try {
            InputStream is = getClass().getResourceAsStream("/fonts/Old-Soviet.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(sizeFont);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setEditable(false);
        setFont(customFont);
        setBackground(null);
    }
}
