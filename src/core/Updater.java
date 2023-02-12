package src.core;

import src.core.Emitter.ActionControl;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Updater {
    private Timer timer;
    private long time, period;
    private Consumer<Long> fn;

    public Updater(long period, Consumer<Long> fn) {
        timer = new Timer();
        this.period = period;
        this.fn = fn;
    }

    public long getTime() {
        return time;
    }

    public void stop() {
        timer.cancel();
    }

    public void start() {
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time += period;
                fn.accept(time);
            }
        }, 0, period);
    }

    public void clear() {
        time = 0;
        stop();
    }
}
