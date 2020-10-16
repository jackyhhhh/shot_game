package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bee extends FlyingObject implements Award{
    private static List<BufferedImage> images;
    static {
        images = new ArrayList<>();
        for(int i=0;i<5;i++){
            images.add(loadImage("bee"+i+".png"));
        }
    }

    private int xSpeed,ySpeed;
    private int awardType;  //奖励类型:0->命,1->分
    Bee() {
        super(87, 72);
        xSpeed = 1;
        ySpeed = 2;
        Random rand = new Random();
        awardType = rand.nextInt(2);
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
    public boolean outOfBounds() {
        return this.y >= World.HEIGHT;
    }

    @Override
    public int getType() {
        return awardType;
    }
}
