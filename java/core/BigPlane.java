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
    public BufferedImage getImage() {
        if(isLife()){
            return images.get(0);
        }else if(isDead()){
            BufferedImage img = images.get(deadIndex++);
            if(deadIndex == images.size()){
                state = REMOVE;
            }
            return img;
        }
        return null;
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
    public int getScore(){
        return 3;
    }
}
