package core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bee extends FlyingObject implements Enemy{
    private static final List<BufferedImage> images;
    static {
        images = new ArrayList<>();
        for(int i=0;i<5;i++){
            images.add(loadImage("images"+ File.separator+"bee"+i+".png"));
        }
    }

    private final int awardType;  //奖励类型:0->命,1->分
    Bee() {
        super(87, 72);
        xSpeed = 1;
        ySpeed = 2;
        Random rand = new Random();
        awardType = rand.nextInt(2);
        health = 3;
    }

    @Override
    public void step() {
        x += xSpeed;
        y += ySpeed;
        if(x<=0 || x>=World.WIDTH-this.width){
            xSpeed *= -1;
        }
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
    public int getScore() {
        return 0;
    }

    @Override
    public void setSpeedByLevel(int level) {
        xSpeed += level - 1;
        ySpeed += level - 1;
    }
}
