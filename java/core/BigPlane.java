package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BigPlane extends FlyingObject {
    private static List<BufferedImage> images;
    static {
        images = new ArrayList<>();
        for(int i=0;i<5;i++){
            images.add(loadImages("bigairplane"+i+".png"));
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

    int deadIndex = 0;
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
    public boolean outOfBounds() {
        return this.y >= World.HEIGHT;
    }

    public int getScore(){
        return 3;
    }
}
