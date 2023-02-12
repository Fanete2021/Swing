package src.view;

import src.config.Configuration;
import src.config.Keys;
import src.utils.Utils;
import src.core.Emitter.ActionControl;
import src.core.Emitter.Actions;
import src.core.Emitter.Emitter;
import src.core.Emitter.Events;
import src.entity.transport.Bike;
import src.entity.transport.Car;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class ControlPanel extends JPanel {
    private final Emitter emitter;
    private final Configuration config;
    private JButton start, clear, currentTransports;
    private ButtonGroup bgTime;
    private JLabel timeLabel;
    private JRadioButton showTime, hideTime;
    private JCheckBox stats, movementCar, movementBike;
    private JTextField timeSpawnCar, timeSpawnBike, lifetimeCar, lifetimeBike;
    private final String DEFAULT_TIME_SPAWN = "1";
    private JComboBox frequencyCar, priorityCar, priorityBike;
    private JList frequencyBike;

    public ControlPanel(int width, int height, Emitter emitter, Configuration config) {
        super();
        setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
        
        this.emitter = emitter;
        emitter.subscribe(Events.HABITAT, this::triggerAction);
        emitter.subscribe(Events.MENU, this::triggerAction);
        emitter.subscribe(Events.MODAL, this::triggerAction);

        this.config = config;

        createButtons();

        timeLabel = new JLabel("Прошло времени: 0.0с");
        timeLabel.setBounds(150, 150, 200, 20);
        add(timeLabel);

        createRadioButtons();

        createCheckBox();
        createTextField();
        createComboBox();
        createList();
    }

    private void triggerAction(ActionControl actionControl) {
        switch (actionControl.action) {
            case CLEAR:
            case START:
                timeLabel.setText("");
                switchButton();
                break;
            case UPDATE_TIME:
                timeLabel.setText("Прошло времени: " + (float)actionControl.state + "с");
                break;
            case IS_SHOW_TIME:
                changeStateInfoTime((boolean)actionControl.state);
                break;
            case IS_SHOW_STATS:
                stats.setSelected((boolean)actionControl.state);
                break;
            default: break;
        }
    }

    private void createButtons() {
        start = FactoryView.createButton("Старт", 20, 20);
        start.addActionListener(e -> {
            switchButton();
            emitter.notify(Events.CONTROL, Actions.START);
        });

        clear = FactoryView.createButton("Очистить", 140, 20);
        clear.setEnabled(false);
        clear.addActionListener(e -> {
            timeLabel.setText("");
            switchButton();
            emitter.notify(Events.CONTROL, Actions.CLEAR);
        });

        currentTransports = FactoryView.createButton("Текущие объекты", 50, 80);
        currentTransports.setSize(150, 50);
        currentTransports.setEnabled(false);
        currentTransports.addActionListener(e -> emitter.notify(Events.CONTROL, Actions.SHOW_CURRENT_TRANSPORT));

        add(start);
        add(clear);
        add(currentTransports);
    }

    private void createRadioButtons() {
        bgTime = new ButtonGroup();

        showTime = FactoryView.createRadioButton(bgTime, "Показать время", 20, 140);
        showTime.addActionListener(e -> {
            timeLabel.setVisible(true);
            emitter.notify(Events.CONTROL, Actions.IS_SHOW_TIME, true);
            config.setProperty(Keys.IS_SHOW_TIME, "true");
        });

        hideTime = FactoryView.createRadioButton(bgTime, "Скрыть время", 20, 160);
        hideTime.addActionListener(e -> {
            timeLabel.setVisible(false);
            emitter.notify(Events.CONTROL, Actions.IS_SHOW_TIME, false);
            config.setProperty(Keys.IS_SHOW_TIME, "false");
        });

        boolean isShowTime = Boolean.parseBoolean(config.getProperty(Keys.IS_SHOW_TIME));
        changeStateInfoTime(isShowTime);

        bgTime.add(showTime);
        bgTime.add(hideTime);

        add(showTime);
        add(hideTime);
    }

    private void createCheckBox() {
        boolean isShowStats = Boolean.parseBoolean(config.getProperty(Keys.IS_SHOW_STATS));
        stats = FactoryView.createCheckBox("Показать информацию", isShowStats, 20, 190);
        stats.addActionListener(e -> {
            emitter.notify(Events.CONTROL, Actions.IS_SHOW_STATS, stats.isSelected());
            config.setProperty(Keys.IS_SHOW_STATS, Boolean.toString(stats.isSelected()));
        });

        boolean isMovingCar = Boolean.parseBoolean(config.getProperty(Keys.IS_MOVING_CAR));
        movementCar = FactoryView.createCheckBox("Движение машин", isMovingCar, 20, 420);
        movementCar.addActionListener(e -> {
            emitter.notify(Events.CONTROL, Actions.IS_MOVEMENT_CAR, movementCar.isSelected());
            config.setProperty(Keys.IS_MOVING_CAR, Boolean.toString(movementCar.isSelected()));
        });

        boolean isMovingBike = Boolean.parseBoolean(config.getProperty(Keys.IS_MOVING_BIKE));
        movementBike = FactoryView.createCheckBox("Движение мотоциклов", isMovingBike, 20, 440);
        movementBike.addActionListener(e -> {
            emitter.notify(Events.CONTROL, Actions.IS_MOVEMENT_BIKE, movementBike.isSelected());
            config.setProperty(Keys.IS_MOVING_BIKE, Boolean.toString(movementBike.isSelected()));
        });


        add(stats);
        add(movementCar);
        add(movementBike);
    }

    private void createTextField() {
        timeSpawnCar = new JTextField(config.getProperty(Keys.GENERATION_TIME_CAR));
        customizeTextFieldAndAddToPanel("Время генерации машины", 20, 220, timeSpawnCar);
        timeSpawnCar.addActionListener(e -> {
            if (!checkErrorFromField(timeSpawnCar)) {
                float value = Float.parseFloat(timeSpawnCar.getText());
                Car.generationTime = value;
                config.setProperty(Keys.GENERATION_TIME_CAR, timeSpawnCar.getText());
            } else {
                timeSpawnCar.setText(DEFAULT_TIME_SPAWN);
                config.setProperty(Keys.GENERATION_TIME_CAR, DEFAULT_TIME_SPAWN);
            }
        });

        timeSpawnBike = new JTextField(config.getProperty(Keys.GENERATION_TIME_BIKE));
        customizeTextFieldAndAddToPanel("Время генерации мотоцикла", 20, 240, timeSpawnBike);
        timeSpawnBike.addActionListener(e -> {
            if (!checkErrorFromField(timeSpawnBike)) {
                float value = Float.parseFloat(timeSpawnBike.getText());
                Bike.generationTime = value;
                config.setProperty(Keys.GENERATION_TIME_BIKE, timeSpawnBike.getText());
            } else {
                timeSpawnBike.setText(DEFAULT_TIME_SPAWN);
                config.setProperty(Keys.GENERATION_TIME_BIKE, DEFAULT_TIME_SPAWN);
            }
        });

        lifetimeCar = new JTextField(config.getProperty(Keys.LIFE_TIME_CAR));
        customizeTextFieldAndAddToPanel("Время жизни машины", 20, 370, lifetimeCar);
        lifetimeCar.addActionListener(e ->
            eventLifetimeField(lifetimeCar, Actions.CHANGE_LIFETIME_CAR, Keys.LIFE_TIME_CAR)
        );

        lifetimeBike = new JTextField(config.getProperty(Keys.LIFE_TIME_BIKE));
        customizeTextFieldAndAddToPanel("Время жизни мотоцикла", 20, 390, lifetimeBike);
        lifetimeBike.addActionListener(e ->
            eventLifetimeField(lifetimeBike, Actions.CHANGE_LIFETIME_BIKE, Keys.LIFE_TIME_BIKE)
        );
    }

    private void eventLifetimeField(JTextField field, Actions action, Keys key) {
        if (!checkErrorFromField(field)) {
            float value = Float.parseFloat(field.getText());
            emitter.notify(Events.CONTROL, action, value);
            config.setProperty(key, field.getText());
        } else {
            field.setText(DEFAULT_TIME_SPAWN);
            config.setProperty(key, DEFAULT_TIME_SPAWN);
        }
    }

    private void createComboBox() {
        Vector<String> percentages = Utils.generateRange(0, 100, 10, "%");
        Vector<String> priorities = Utils.generateRange(1, 6, 1, "");
        int priorityThread;
        
        createDescriptionLabel("Шанс генерации машины", 20, 280);
        float chance = Float.parseFloat(config.getProperty(Keys.CHANCE_GENERATION_CAR));
        int selectedIndex = (int)(chance * 10);
        frequencyCar = FactoryView.createComboBox(percentages, 200, 280, selectedIndex);
        frequencyCar.addActionListener(e -> {
            float frequency = Utils.parsePercentages((String)frequencyCar.getSelectedItem());
            Car.frequency = frequency;
            config.setProperty(Keys.CHANCE_GENERATION_CAR, Float.toString(frequency));
        });

        createDescriptionLabel("Приоритет потока машин", 20, 470);
        priorityThread = Integer.parseInt(config.getProperty(Keys.PRIORITY_THREAD_CAR));
        priorityCar = FactoryView.createComboBox(priorities, 200, 470, priorityThread);
        priorityCar.addActionListener(e -> {
            int priority = Integer.parseInt((String)priorityCar.getSelectedItem());
            emitter.notify(Events.CONTROL, Actions.CHANGE_PRIORITY_THREAD_CAR, priority);
            config.setProperty(Keys.PRIORITY_THREAD_CAR, Integer.toString(priority - 1));
        });

        createDescriptionLabel("Приоритет потока мотоциклов", 20, 490);
        priorityThread = Integer.parseInt(config.getProperty(Keys.PRIORITY_THREAD_BIKE));
        priorityBike = FactoryView.createComboBox(priorities, 200, 490, priorityThread);
        priorityBike.addActionListener(e -> {
            int priority = Integer.parseInt((String)priorityBike.getSelectedItem());
            emitter.notify(Events.CONTROL, Actions.CHANGE_PRIORITY_THREAD_BIKE, priority);
            config.setProperty(Keys.PRIORITY_THREAD_BIKE, Integer.toString(priority - 1));
        });

        add(frequencyCar);
        add(priorityCar);
        add(priorityBike);
    }

    private void createList() {
        Vector<String> percentages = Utils.generateRange(0, 100, 10, "%");

        createDescriptionLabel("Шанс генерации мотоцикла", 20, 300);
        frequencyBike = new JList(percentages);
        frequencyBike.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        frequencyBike.setVisibleRowCount(2);
        frequencyBike.setBounds(20, 320, 200, 40);
        frequencyBike.setBackground(null);
        int selectedIndex = (int)(Float.parseFloat(config.getProperty(Keys.CHANCE_GENERATION_BIKE)) * 10);
        frequencyBike.setSelectedIndex(selectedIndex);
        frequencyBike.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                float frequency = Utils.parsePercentages((String)frequencyBike.getSelectedValue());
                Bike.frequency = frequency;
                config.setProperty(Keys.CHANCE_GENERATION_BIKE, Float.toString(frequency));
            }
        });

        add(frequencyBike);
    }

    private void switchButton() {
        if (start.isEnabled()) {
            start.setEnabled(false);
            clear.setEnabled(true);
            currentTransports.setEnabled(true);
        } else {
            start.setEnabled(true);
            clear.setEnabled(false);
            currentTransports.setEnabled(false);
        }
    }

    private void customizeTextFieldAndAddToPanel(String description, int x, int y, JTextField field) {
        createDescriptionLabel(description, x, y);
        field.setBounds(x + 180, y, 50, 20);
        add(field);
    }

    private void createDescriptionLabel(String description, int x, int y) {
        JLabel desc = new JLabel(description);
        desc.setBounds(x, y, 200, 20);
        add(desc);
    }

    private boolean checkErrorFromField(JTextField field) {
        float value;

        try {
            value = Float.parseFloat(field.getText());
        } catch (NumberFormatException ex) {
            ErrorModal error = new ErrorModal("Вы ввели некорректный символ");
            return true;
        }

        if (field.getText().length() == 0 || value <= 0) {
            ErrorModal error = new ErrorModal("Поле содержит некорректное значение");
            return true;
        }

        return false;
    }

    private void changeStateInfoTime(boolean isShow) {
        timeLabel.setVisible(isShow);

        if (isShow) {
            showTime.setSelected(true);
        } else {
            hideTime.setSelected(true);
        }
    }
}
