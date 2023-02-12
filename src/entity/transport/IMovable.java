package src.entity.transport;

public interface IMovable {
    int getX();
    int getY();

    void setX(int x);
    void setY(int y);

    int getSpeed();
    void setSpeed(int speed);
}
