package src.core.Emitter;

public class ActionControl {
    public Object state;
    public Actions action;

    public ActionControl(Actions action, Object state) {
        this.state = state;
        this.action = action;
    }

    public ActionControl(Actions action) {
        this.action = action;
    }
}
