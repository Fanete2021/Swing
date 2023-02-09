package Clasess.Emitter;

public class ActionControl {
    public int state = 0;
    public String text = "";
    public Actions action;

    public ActionControl(Actions action, int state) {
        this.state = state;
        this.action = action;
    }

    public ActionControl(Actions action) {
        this.action = action;
    }
}
