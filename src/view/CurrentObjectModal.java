package src.view;

import javax.swing.*;

public class CurrentObjectModal extends JDialog {
    private final JButton ok;
    private final JTextArea statistics;

    public CurrentObjectModal(String title, String stats) {
        setTitle(title);
        setLayout(null);

        statistics = FactoryView.createTextArea(stats, 11f);

        ok = FactoryView.createButton("Закрыть", 110, 260);
        ok.addActionListener(e -> setVisible(false));

        JScrollPane scroll = new JScrollPane(statistics);
        scroll.setBounds(10, 10, 290, 240);
        add(scroll);
        add(ok);

        setSize(320, 350);
    }

    public void updateText(String text) {
        statistics.setText(text);
        statistics.repaint();
    }
}
