package core;

import com.sun.org.apache.regexp.internal.RE;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bee extends FlyingObject{
    private static List<BufferedImage> images;
    static {
        images = new ArrayList<>();
        for(int i=0;i<5;i++){
            images.add(loadImages("bee"+i+".png"));
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

    int deadIndex = 0;
    @Override
    public BufferedImage getImage() {
        if(isLife()){
            return images.get(0);
        }else if(isDead()){
            BufferedImage img = images.get(deadIndex++);
            if(deadIndex == images.size()) {
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
}
