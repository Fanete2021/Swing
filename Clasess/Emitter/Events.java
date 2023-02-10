package Clasess.Emitter;

public enum Events {
    CONTROL ("Screen:Control"),
    HABITAT ("Habitat"),
    MENU ("Screen:Menu");

    private String title;

    Events(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
