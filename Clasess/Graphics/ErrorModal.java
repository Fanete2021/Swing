package Clasess.Graphics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorModal extends JDialog {
    private final Button ok;
    private final TextArea error;

    public ErrorModal(String textError) {
        setTitle("Ошибка");
        setLayout(null);

        error = new TextArea(textError, 12f);
        error.setBounds(10, 10, 190, 100);

        ok = new Button("ОК", 50, 110, getOkActionListener());

        add(error);
        add(ok);

        setSize(200, 200);
        setVisible(true);
    }

    private ActionListener getOkActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        };
    }
}
