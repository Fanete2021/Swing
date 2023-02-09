package Clasess.Emitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Emitter {
    HashMap<String, List<Consumer<ActionControl>>> listeners;

    public Emitter() {
        listeners = new HashMap<>();
    }

    public void emit(String event, ActionControl arg) {
        for (Consumer<ActionControl> fn: listeners.get(event)) {
            fn.accept(arg);
        }
    }

    public void subscribe(String event, Consumer<ActionControl> fn) {
        if (!listeners.containsKey(event)) {
            List<Consumer<ActionControl>> list = new ArrayList<>();
            listeners.put(event, list);
        }

        listeners.get(event).add(fn);
    }
}
