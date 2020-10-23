package core;

import java.awt.image.BufferedImage;
import java.io.File;

public class Bullet extends FlyingObject{
    private static final BufferedImage image;
    static {
        image = loadImage("images"+ File.separator+"bullet.png");
    }

    Bullet(int x, int y) {
        super(10, 21, x, y);
        ySpeed = 5;
    }

    @Override
    public void step() {
        y -= ySpeed;
    }

    @Override
    public BufferedImage getImage() {
        if(isAlive()){
            return image;
        }
        status = REMOVE;
        return null;
    }

    @Override
    public boolean outOfBounds() {
        return this.y <= -this.height;
    }

    public void setSpeedByLevel(int level){
        ySpeed += level - 1;
    }
}
