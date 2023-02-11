package Clasess.Graphics;

import Clasess.Core.Utils;
import Clasess.Emitter.ActionControl;
import Clasess.Emitter.Actions;
import Clasess.Emitter.Emitter;
import Clasess.Emitter.Events;
import Clasess.Entity.Bike;
import Clasess.Entity.Car;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ControlPanel extends JPanel {
    private final Emitter emitter;
    private final Button start, stop, currentTransports;
    private final ButtonGroup bgTime;
    private final JLabel timeLabel;
    private final JRadioButton showTime, hideTime;
    private final JCheckBox stats;
    private final JTextField timeSpawnCar, timeSpawnBike;
    private final String DEFAULT_TIME = "100";
    private final JComboBox frequencyCar;
    private final JList frequencyBike;
    private final JTextField lifetimeCar, lifetimeBike;
    private final JCheckBox movementCar, movementBike;
    private final JComboBox priorityCar, priorityBike;

    public ControlPanel(int width, int height, Emitter emitter) {
        super();
        setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
        
        this.emitter = emitter;
        emitter.subscribe(Events.HABITAT.getTitle(), this::triggerAction);
        emitter.subscribe(Events.MENU.getTitle(), this::triggerAction);

        start = new Button("Старт", 20, 20, getStartActionListener());
        add(start);
        stop = new Button("Стоп", 140, 20, getStopActionListener());
        stop.setEnabled(false);
        add(stop);
        currentTransports = new Button("Текущие объекты", 260, 20, getShowCurrentTransportActionListener());
        currentTransports.setBounds(280, 20, 200, 50);
        currentTransports.setEnabled(false);
        add(currentTransports);

        bgTime = new ButtonGroup();
        showTime = new JRadioButton("Показать время");
        customizeRadioAndAddToPanel(bgTime, showTime, 20, 100, getShowActionListener());
        hideTime = new JRadioButton("Скрыть время");
        customizeRadioAndAddToPanel(bgTime, hideTime, 20, 130, getHideActionListener());
        hideTime.setSelected(true);

        timeLabel = new JLabel("Прошло времени: 0.0с");
        timeLabel.setBounds(200, 115, 200, 20);
        timeLabel.setVisible(false);
        add(timeLabel);

        stats = new JCheckBox("Показать информацию");
        stats.setBounds(20, 170, 200, 20);
        stats.setFocusPainted(false);
        stats.addActionListener(getStatsActionListener());
        add(stats);

        timeSpawnCar = new JTextField("1000");
        customizeTextFieldAndAddToPanel("Время генерации машины", 20, 220,
                timeSpawnCar, 75, 250, getTimeSpawnCarActionListener());

        timeSpawnBike = new JTextField("1000");
        customizeTextFieldAndAddToPanel("Время генерации мотоцикла", 250, 220,
                timeSpawnBike, 305, 250, getTimeSpawnBikeActionListener());

        Vector<String> percentages = Utils.generatePercentages(0, 100, 10);

        JLabel infoFrequencyCar = new JLabel("Шанс генерации машины");
        infoFrequencyCar.setBounds(20, 280, 200, 20);
        add(infoFrequencyCar);
        frequencyCar = new JComboBox(percentages);
        frequencyCar.setMaximumRowCount(5);
        frequencyCar.setBounds(75, 310, 70, 20);
        frequencyCar.setSelectedIndex(9);
        frequencyCar.addActionListener(getFrequencyCarActionListener());
        add(frequencyCar);

        JLabel infoFrequencyBike = new JLabel("Шанс генерации мотоцикла");
        infoFrequencyBike.setBounds(250, 280, 200, 20);
        add(infoFrequencyBike);
        frequencyBike = new JList(percentages);
        frequencyBike.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        frequencyBike.setVisibleRowCount(2);
        frequencyBike.setBounds(250, 310, 200, 40);
        frequencyBike.setBackground(null);
        frequencyBike.setSelectedIndex(4);
        frequencyBike.addListSelectionListener(getFrequencyBikeSelectionListener());
        add(frequencyBike);

        lifetimeCar = new JTextField("5000");
        customizeTextFieldAndAddToPanel("Время жизни машины", 20, 370,
                lifetimeCar, 75, 400, getLifetimeCarActionListener());

        lifetimeBike = new JTextField("3000");
        customizeTextFieldAndAddToPanel("Время жизни мотоцикла", 250, 370,
                lifetimeBike, 305, 400, getLifetimeBikeActionListener());

        movementCar = new JCheckBox("Движение машин");
        movementCar.setBounds(20, 450, 200, 20);
        movementCar.setFocusPainted(false);
        movementCar.setSelected(true);
        movementCar.addActionListener(getMovementCarActionListener());
        add(movementCar);

        movementBike = new JCheckBox("Движение мотоциклов");
        movementBike.setBounds(240, 450, 200, 20);
        movementBike.setFocusPainted(false);
        movementBike.setSelected(true);
        movementBike.addActionListener(getMovementBikeActionListener());
        add(movementBike);

        Vector<String> priority = Utils.generatePercentages(1, 6, 1);

        JLabel infoPriorityCar = new JLabel("Приоритет потока для машин");
        infoFrequencyCar.setBounds(20, 500, 200, 20);
        add(infoFrequencyCar);
        priorityCar = new JComboBox(priority);
        priorityCar.setMaximumRowCount(3);
        priorityCar.setBounds(75, 530, 70, 20);
        priorityCar.setSelectedIndex(0);
        priorityCar.addActionListener(getPriorityCarActionListener());
        add(priorityCar);

        JLabel infoPriorityBike = new JLabel("Приоритет потока для машин");
        infoPriorityBike.setBounds(220, 500, 200, 20);
        add(infoPriorityBike);
        priorityBike = new JComboBox(priority);
        priorityBike.setMaximumRowCount(3);
        priorityBike.setBounds(275, 530, 70, 20);
        priorityBike.setSelectedIndex(0);
        priorityBike.addActionListener(getPriorityBikeActionListener());
        add(priorityBike);
    }

    private void triggerAction(ActionControl actionControl) {
        switch (actionControl.action) {
            case STOP:
            case START:
                switchButton();
                break;
            case UPDATE_TIME:
                String time = Utils.toStringTime(actionControl.state);
                timeLabel.setText("Прошло времени: " + time + "с");
                break;
            case HIDE_TIME:
                timeLabel.setVisible(false);
                hideTime.setSelected(true);
                break;
            case SHOW_TIME:
                timeLabel.setVisible(true);
                showTime.setSelected(true);
                break;
            case SHOW_STATS:
                stats.setSelected(true);
                break;
            case HIDE_STATS:
                stats.setSelected(false);
                break;
            default: break;
        }
    }

    private ActionListener getStartActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchButton();
                ActionControl action = new ActionControl(Actions.START);
                emitter.emit(Events.CONTROL.getTitle(), action);
            }
        };
    }

    private ActionListener getStopActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchButton();
                ActionControl action = new ActionControl(Actions.STOP);
                emitter.emit(Events.CONTROL.getTitle(), action);
            }
        };
    }

    private ActionListener getShowCurrentTransportActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionControl action = new ActionControl(Actions.SHOW_CURRENT_TRANSPORT);
                emitter.emit(Events.CONTROL.getTitle(), action);
            }
        };
    }

    private void switchButton() {
        if (start.isEnabled()) {
            start.setEnabled(false);
            stop.setEnabled(true);
            currentTransports.setEnabled(true);
        } else {
            start.setEnabled(true);
            stop.setEnabled(false);
            currentTransports.setEnabled(false);
        }
    }

    private void customizeRadioAndAddToPanel(ButtonGroup bg, JRadioButton rb, int x, int y, ActionListener actionListener) {
        rb.setBounds(x, y, 150, 20);
        rb.setFocusPainted(false);
        rb.setContentAreaFilled(false);
        rb.addActionListener(actionListener);
        bg.add(rb);
        add(rb);
    }

    private ActionListener getShowActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setVisible(true);
                ActionControl action = new ActionControl(Actions.SHOW_TIME);
                emitter.emit(Events.CONTROL.getTitle(), action);
            }
        };
    }

    private ActionListener getHideActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setVisible(false);
                ActionControl action = new ActionControl(Actions.HIDE_TIME);
                emitter.emit(Events.CONTROL.getTitle(), action);
            }
        };
    }

    private ActionListener getStatsActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Actions action = stats.isSelected() ? Actions.SHOW_STATS : Actions.HIDE_STATS;
                ActionControl actionControl = new ActionControl(action);
                emitter.emit(Events.CONTROL.getTitle(), actionControl);
            }
        };
    }

    private void customizeTextFieldAndAddToPanel(String description, int xDesc, int yDesc,
                                                 JTextField field, int xFild, int yFild, ActionListener actionList) {
        JLabel desc = new JLabel(description);
        desc.setBounds(xDesc, yDesc, 200, 20);

        field.setBounds(xFild, yFild, 50, 20);
        field.addActionListener(actionList);

        add(desc);
        add(field);
    }

    private ActionListener getTimeSpawnCarActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkErrorFromSpawnField(timeSpawnCar)) {
                    int value = Integer.parseInt(timeSpawnCar.getText());
                    Car.generationTime = value;
                } else {
                    timeSpawnCar.setText(DEFAULT_TIME);
                }
            }
        };
    }

    private ActionListener getTimeSpawnBikeActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkErrorFromSpawnField(timeSpawnBike)) {
                    int value = Integer.parseInt(timeSpawnBike.getText());
                    Bike.generationTime = value;
                } else {
                    timeSpawnBike.setText(DEFAULT_TIME);
                }
            }
        };
    }

    private ActionListener getFrequencyCarActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float frequency = Utils.parsePercentages((String)frequencyCar.getSelectedItem());
                Car.frequency = frequency;
            }
        };
    }

    private ListSelectionListener getFrequencyBikeSelectionListener() {
        return new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    float frequency = Utils.parsePercentages((String)frequencyBike.getSelectedValue());
                    Bike.frequency = frequency;
                }
            }
        };
    }

    private ActionListener getLifetimeCarActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkErrorFromSpawnField(lifetimeCar)) {
                    int value = Integer.parseInt(lifetimeCar.getText());
                    ActionControl actionControl = new ActionControl(Actions.CHANGE_LIFETIME_CAR, value);
                    emitter.emit(Events.CONTROL.getTitle(), actionControl);
                } else {
                    lifetimeCar.setText(DEFAULT_TIME);
                }
            }
        };
    }

    private ActionListener getLifetimeBikeActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkErrorFromSpawnField(lifetimeBike)) {
                    int value = Integer.parseInt(lifetimeBike.getText());
                    ActionControl actionControl = new ActionControl(Actions.CHANGE_LIFETIME_BIKE, value);
                    emitter.emit(Events.CONTROL.getTitle(), actionControl);
                } else {
                    lifetimeBike.setText(DEFAULT_TIME);
                }
            }
        };
    }

    private boolean checkErrorFromSpawnField(JTextField field) {
        int value = 100;

        try {
            value = Integer.parseInt(field.getText());
        } catch (NumberFormatException ex) {
            ErrorModal error = new ErrorModal("Вы ввели символ");
            return true;
        }

        if (field.getText().length() < 3) {
            ErrorModal error = new ErrorModal("Вы не можете вводить меньше 3 цифр");
            return true;
        }

        return false;
    }

    private ActionListener getMovementCarActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Actions action = movementCar.isSelected() ? Actions.MOVEMENT_CAR_TRUE : Actions.MOVEMENT_CAR_FALSE;
                ActionControl actionControl = new ActionControl(action);
                emitter.emit(Events.CONTROL.getTitle(), actionControl);
            }
        };
    }

    private ActionListener getMovementBikeActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Actions action = movementBike.isSelected() ? Actions.MOVEMENT_BIKE_TRUE : Actions.MOVEMENT_BIKE_FALSE;
                ActionControl actionControl = new ActionControl(action);
                emitter.emit(Events.CONTROL.getTitle(), actionControl);
            }
        };
    }

    private ActionListener getPriorityCarActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int priority = (int)(Utils.parsePercentages((String)priorityCar.getSelectedItem()) * 100);
                ActionControl actionControl = new ActionControl(Actions.CHANGE_PRIORITY_THREAD_CAR, priority);
                emitter.emit(Events.CONTROL.getTitle(), actionControl);
            }
        };
    }

    private ActionListener getPriorityBikeActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int priority = (int)(Utils.parsePercentages((String)priorityBike.getSelectedItem()) * 100);
                ActionControl actionControl = new ActionControl(Actions.CHANGE_PRIORITY_THREAD_BIKE, priority);
                emitter.emit(Events.CONTROL.getTitle(), actionControl);
            }
        };
    }
}
