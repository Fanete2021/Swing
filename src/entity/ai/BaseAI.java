package src.entity.ai;

import src.view.TransportLabel;

import java.util.List;

public abstract class BaseAI extends Thread {
    protected final List<TransportLabel> transports;
    public Object lock;
    public boolean isWorking = false;

    public BaseAI(List<TransportLabel> list) {
        transports = list;
        lock = new Object();
        setPriority(1);
    }

    protected abstract void move(TransportLabel transport);

    public abstract void run();
}
