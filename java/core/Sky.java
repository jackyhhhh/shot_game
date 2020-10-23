package core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Sky extends FlyingObject {
    private static final BufferedImage image;
    static {
        image = loadImage("images"+ File.separator+"background.png");
    }

    private int y1;     //y1坐标(为了图片轮换)
    Sky() {
        super(World.WIDTH, World.HEIGHT, 0, 0);
        ySpeed = 1;
        y1 = -height;
    }

    /**
     * background image always move,
     * so flying objects look like flying
     */
    @Override
    public void step() {
        y += ySpeed;
        y1 += ySpeed;
        if(y >= World.HEIGHT){
            y = -World.HEIGHT;
        }
        if(y1 >= World.HEIGHT){
            y1 = -World.HEIGHT;
        }
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    public void paintObject(Graphics g) {
        g.drawImage(getImage(), x, y, null);
        g.drawImage(getImage(), x, y1, null);
    }
}
