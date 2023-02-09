package Clasess.Graphics;

import Clasess.Core.Utils;
import Clasess.Emitter.ActionControl;
import Clasess.Emitter.Actions;
import Clasess.Emitter.Emitter;
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
    private final Button start, stop;
    private final ButtonGroup bgTime;
    private final JLabel timeLabel;
    private final JRadioButton showTime, hideTime;
    private final JCheckBox stats;
    private final JTextField timeSpawnCar, timeSpawnBike;
    private final String DEFAULT_TIME = "100";
    private final JComboBox frequencyCar;
    private final JList frequencyBike;

    public ControlPanel(int width, int height, Emitter emitter) {
        super();
        setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
        
        this.emitter = emitter;
        emitter.subscribe("Screen:Menu", this::triggerAction);
        emitter.subscribe("Habitat", this::triggerAction);

        start = new Button("Старт", 20, 20, getStartActionListener());
        add(start);
        stop = new Button("Стоп", 140, 20, getStopActionListener());
        stop.setEnabled(false);
        add(stop);

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

        JLabel infoFrequencyCar = new JLabel("Шанс генерации машины");
        infoFrequencyCar.setBounds(20, 280, 200, 20);
        add(infoFrequencyCar);
        Vector<String> percentages = Utils.generatePercentages(10, 100, 10);
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
        frequencyBike.setBounds(305, 310, 40, 180);
        frequencyBike.setSelectedIndex(4);
        frequencyBike.addListSelectionListener(getFrequencyBikeSelectionListener());
        add(frequencyBike);
    }

    private void triggerAction(ActionControl actionControl) {
        switch (actionControl.action) {
            case STOP:
            case START:
                switchButton();
                break;
            case UPDATE_TIME:
                UpdateTime(actionControl.state);
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
                emitter.emit("Screen:Control", action);
            }
        };
    }

    private ActionListener getStopActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchButton();
                ActionControl action = new ActionControl(Actions.STOP);
                emitter.emit("Screen:Control", action);
            }
        };
    }

    private void switchButton() {
        if (start.isEnabled()) {
            start.setEnabled(false);
            stop.setEnabled(true);
        } else {
            start.setEnabled(true);
            stop.setEnabled(false);
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
                emitter.emit("Screen:Control", action);
            }
        };
    }

    private ActionListener getHideActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setVisible(false);
                ActionControl action = new ActionControl(Actions.HIDE_TIME);
                emitter.emit("Screen:Control", action);
            }
        };
    }

    private void UpdateTime(int time) {
        int seconds = time / 1000;
        int ms = time % 1000;

        timeLabel.setText("Прошло времени: " + seconds + "." + ms / 100 + "с");
    }

    private ActionListener getStatsActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Actions action = stats.isSelected() ? Actions.SHOW_STATS : Actions.HIDE_STATS;
                ActionControl actionControl = new ActionControl(action);
                emitter.emit("Screen:Control", actionControl);
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
}
