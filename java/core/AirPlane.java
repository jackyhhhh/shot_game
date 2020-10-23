package core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AirPlane extends FlyingObject implements Enemy{
    private static final List<BufferedImage> images;
    static {
        images = new ArrayList<>();
        for(int i=0;i<5;i++){
            images.add(loadImage("images"+ File.separator+"airplane" + i + ".png"));
        }
    }

    private final int awardType;
    AirPlane() {
        super(69, 46);
        awardType = Enemy.SCORE;
    }

    @Override
    public void step() {
        y += ySpeed;
    }

    @Override
    public BufferedImage getImage() {
        if(isAlive()){
            return images.get(0);
        }else if(isDead()){
            BufferedImage img = images.get(deadIndex++);
            if(deadIndex == images.size()){
                status = REMOVE;
            }
            return img;
        }
        return null;
    }

    @Override
    public boolean outOfBounds() {
        return this.y >= World.HEIGHT;
    }

    @Override
    public int getAwardType() {
        return awardType;
    }

    @Override
    public void setSpeedByLevel(int level) {
        ySpeed += level - 1;
    }

    @Override
    public int getScore() {
        return 1;
    }
}
