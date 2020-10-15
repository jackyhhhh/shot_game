package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AirPlane extends FlyingObject{
    private static List<BufferedImage> images;
    static {
        images = new ArrayList<>();
        for(int i=0;i<5;i++){
            images.add(loadImages("airplane" + i + ".png"));
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

    private int deadIndex = 0; // 小敌机死了时, 图片轮播开始的下标(爆炸效果)
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
}
