package src.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TransportsPanel extends JPanel {
    private List<TransportLabel> transportsLabels;

    public TransportsPanel(int width, int height) {
        super();
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
        transportsLabels = new ArrayList<>();

        URL imageURL = getClass().getResource("/src/resources/road.png");
        Image backgroundImage = new ImageIcon(imageURL).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel roadLabel = new JLabel(new ImageIcon(backgroundImage));
        roadLabel.setBounds(0, 0, width, height);
        add(roadLabel);
    }

    public void addTransport(TransportLabel transport) {
        add(transport);
        setComponentZOrder(transport, 0);
        transportsLabels.add(transport);
    }

    public void removeTransport(TransportLabel transport) {
        remove(transport);
    }

    public void removeTransports() {
        for (int i = 0; i < transportsLabels.size(); i++) {
            remove(transportsLabels.get(i));
        }

        transportsLabels.clear();
    }
}
