package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BigPlane extends FlyingObject implements Enemy{
    private static List<BufferedImage> images;
    static {
        images = new ArrayList<>();
        for(int i=0;i<5;i++){
            images.add(loadImage("bigairplane"+i+".png"));
        }
    }

    private final int speed;
    BigPlane() {
        super(91, 116);
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

    public int getScore(){
        return 3;
    }
}
