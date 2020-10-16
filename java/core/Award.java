package core;

public interface Award {
    public static final int DOUBLE_FIRE = 0;
    public static final int LIVES = 1;

    /**
     * get the type of award
     * @return DOUBLE_FIRE or LIVES
     */
    int getType();
}
