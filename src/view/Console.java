package src.view;

import src.core.Emitter.ActionControl;
import src.core.Emitter.Actions;
import src.core.Emitter.Emitter;
import src.core.Emitter.Events;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Console extends JDialog implements Runnable {
    private JTextArea result;
    private Emitter emitter;
    private Scanner scanner;

    public Console(Emitter emitter) {
        setTitle("Консоль");
        setLayout(null);

        this.emitter = emitter;
        emitter.subscribe(Events.HABITAT, this::triggerAction);

        JTextField textArea = new JTextField();
        textArea.setBounds(10, 10, 370, 30);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        result = FactoryView.createTextArea("", 14f);
        result.setBounds(100, 60, 300, 50);


        setSize(400, 140);

        add(textArea);
        add(result);

        PipedOutputStream outputFromField = new PipedOutputStream();
        try {
            PipedInputStream inputFromField = new PipedInputStream(outputFromField);
            scanner = new Scanner(inputFromField);
            PrintStream fieldOutput = new PrintStream(outputFromField);

            textArea.addActionListener(e -> {
                fieldOutput.println(textArea.getText());
                textArea.setText("");
            });

            new Thread(this).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void triggerAction(ActionControl actionControl) {
        switch (actionControl.action) {
            case DELETE_BIKES:
                result.setText("Удалено " + (int)actionControl.state + " мотоциклов");
            default: break;
        }
    }

    @Override
    public void run() {
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().toLowerCase().trim();

            if (command.startsWith("сократить число мотоциклов на ") && command.endsWith("%")) {
                command = command.replace("сократить число мотоциклов на ", "").replace("%", "");
                int percentages = Integer.parseInt(command);

                emitter.notify(Events.MENU, Actions.DELETE_BIKES, percentages);
            } else {
                result.setText("Введена неверная команда");
            }
        }
    }
}
