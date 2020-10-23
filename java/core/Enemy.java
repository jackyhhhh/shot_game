package core;

public interface Enemy {
    int HEALTH = 0;
    int DOUBLE_FIRE = 1;
    int SCORE = 2;

    int getAwardType();
    int getScore();
    void setSpeedByLevel(int level);
}
