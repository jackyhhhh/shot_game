package core;

public abstract class FlyingObject {
    protected int ySpeed;
    protected int xSpeed;
    protected int width;
    protected int height;
    protected int yPos;

    public FlyingObject(int width, int height, int ySpeed){

    }

    public abstract void step();
}
