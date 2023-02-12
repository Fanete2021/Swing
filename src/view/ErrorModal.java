package src.view;

import javax.swing.*;

public class ErrorModal extends JDialog {
    private final JButton ok;
    private final JTextArea error;

    public ErrorModal(String textError) {
        setTitle("Ошибка");
        setLayout(null);

        error = FactoryView.createTextArea(textError, 12f);
        error.setBounds(10, 10, 190, 100);

        ok = FactoryView.createButton("ОК", 50, 110);
        ok.addActionListener(e -> {
            setVisible(false);
            dispose();
        });

        add(error);
        add(ok);

        setSize(200, 200);
        setVisible(true);
    }
}
