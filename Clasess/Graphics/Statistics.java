package Clasess.Graphics;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Statistics extends JComponent {
    private Font customFont;
    private List<String> stats;

    public Statistics() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/Old-Soviet.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clearStats();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(customFont);

        int minY = 25, minX = 0, offsetY = 30;

        for (int i = 0; i < stats.size(); ++i) {
            g2.drawString(stats.get(i), minX,minY + offsetY * i);
        }
    }

    public void addStats(String str) {
        stats.add(str);
        repaint();
    }

    public void clearStats() {
        stats = new ArrayList<String>();
        repaint();
    }
}
