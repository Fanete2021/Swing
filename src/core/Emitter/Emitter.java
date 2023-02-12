package src.core.Emitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Emitter {
    HashMap<Events, List<Consumer<ActionControl>>> listeners;

    public Emitter() {
        listeners = new HashMap<>();
    }

    private void emit(Events event, ActionControl arg) {
        for (Consumer<ActionControl> fn: listeners.get(event)) {
            fn.accept(arg);
        }
    }

    public void subscribe(Events event, Consumer<ActionControl> fn) {
        if (!listeners.containsKey(event)) {
            List<Consumer<ActionControl>> list = new ArrayList<>();
            listeners.put(event, list);
        }

        listeners.get(event).add(fn);
    }

    public void notify(Events event, Actions action, Object state) {
        ActionControl actionControl = new ActionControl(action, state);
        emit(event, actionControl);
    }

    public void notify(Events event, Actions action) {
        ActionControl actionControl = new ActionControl(action);
        emit(event, actionControl);
    }
}
