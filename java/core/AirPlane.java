package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AirPlane extends FlyingObject implements Enemy{
    private static List<BufferedImage> images;
    static {
        images = new ArrayList<>();
        for(int i=0;i<5;i++){
            images.add(loadImage("airplane" + i + ".png"));
        }
    }

    private final int speed;

    AirPlane() {
        super(69, 46);
        speed = 2;
    }

    @Override
    public void step() {
        y += speed;
    }

    @Override
    public boolean outOfBounds() {
        return this.y >= World.HEIGHT;
    }

    @Override
    public int getScore() {
        return 1;
    }
}
