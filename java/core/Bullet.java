package core;

import java.awt.image.BufferedImage;

public class Bullet extends FlyingObject{
    private static final BufferedImage image;
    static {
        image = loadImage("bullet.png");
    }

    private final int speed;
    Bullet(int x, int y) {
        super(10, 21, x, y);
        speed = 3;
    }

    @Override
    public void step() {
        y -= speed;
    }

    @Override
    public BufferedImage getImage() {
        if(isLife()){
            return image;
        }else{
            state = REMOVE;
        }
        return null;
    }

    @Override
    public boolean outOfBounds() {
        return this.y <= -this.height;
    }
}
