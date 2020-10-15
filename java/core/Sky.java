package core;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject {
    private static BufferedImage image;
    static {
        image = loadImages("background.png");
    }

    private int speed;
    private int y1;     //y1坐标(为了图片轮换)
    Sky() {
        super(World.WIDTH, World.HEIGHT, 0, 0);
        speed = 1;
        y1 = -height;
    }

    /**
     * background image always move,
     * so flying objects look like flying
     */
    @Override
    public void step() {
        y += speed;
        y1 += speed;
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
