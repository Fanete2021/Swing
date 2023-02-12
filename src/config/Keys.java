package src.config;

public enum Keys {
    CHANCE_GENERATION_BIKE("chanceGenerationBike"),
    CHANCE_GENERATION_CAR("chanceGenerationCar"),
    IS_MOVING_BIKE("isMovingBike"),
    IS_MOVING_CAR("isMovingCar"),
    IS_SHOW_STATS("isShowStats"),
    IS_SHOW_TIME("isShowTime"),
    LIFE_TIME_BIKE("lifetimeBike"),
    LIFE_TIME_CAR("lifetimeCar"),
    PRIORITY_THREAD_BIKE("priorityThreadBike"),
    PRIORITY_THREAD_CAR("priorityThreadCar"),
    GENERATION_TIME_BIKE("generationTimeBike"),
    GENERATION_TIME_CAR("generationTimeCar");

    private String title;

    Keys(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
