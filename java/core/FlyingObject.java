package core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public abstract class FlyingObject {
    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected int state = LIFE;
    protected int deadIndex = 0;        // 敌机死了时, 图片轮播开始的下标(爆炸效果)

    /**
     * state constants of the flying object
     * Represents that the flyingObject is alive
     */
    public static final int LIFE = 0;

    /**
     * state constants of the flying object
     * Represents that the flyingObject is dead
     */
    public static final int DEAD = 1;

    /**
     * state constants of the flying object
     * Represents that the flyingObject can be removed
     */
    public static final int REMOVE = 2;

    /**
     * constructor:
     * for the flyingObjects who appear with a fixed x-coordinate
     * @param width width of the flyingObject
     * @param height height of the flyingObject
     * @param x the x-coordinate value of the flyingObject's location
     * @param y the y-coordinate value of the flyingObject's location
     */
    FlyingObject(int width, int height, int x, int y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    /**
     * constructor:
     * for the flyingObject who appear with a random x-coordinate
     * @param width width of the flyingObject
     * @param height height of the flyingObject
     */
    FlyingObject(int width, int height){
        this.width = width;
        this.height = height;
        Random rand = new Random();
        this.x = rand.nextInt(World.WIDTH - this.width);
        this.y = -this.height;
    }

    /**
     * load image from disk(static resource)
     * @param fileName the name of the image
     * @return a BufferedImage type image
     */
    public static BufferedImage loadImage(String fileName){
        try {
            return ImageIO.read(FlyingObject.class.getResource(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * the movement of the FlyingObject
     */
    public abstract void step();

    /**
     * get the real-time image of the FlyingObject
     * @return the real-time image
     */
    public abstract BufferedImage getImage();

    /**
     * draw the image of the object on the screen
     * @param g can be understand as a pen
     */
    public void paintObject(Graphics g){
        g.drawImage(getImage(), x, y, null);
    }

    /**
     * judge whether the obj is alive or not
     * @return if alive return true, if not alive return false
     */
    public boolean isLife(){
        return state == LIFE;
    }

    /**
     * judge whether the obj is dead or not
     * @return if dead return true, if not dead return false
     */
    public boolean isDead(){
        return state == DEAD;
    }

    /**
     * judge whether the obj can be removed or not
     * @return if can be removed return true, if can not be removed return false
     */
    public boolean isRemove(){
        return state == REMOVE;
    }

    /**
     * judge whether the obj is out of bounds  or not
     * @return a boolean value
     */
    public abstract boolean outOfBounds();

    /**
     * change the state of the FlyingObject into dead
     */
    public void goDead(){
        state = DEAD;
    }

    /**
     * checking that whether the enemy is hitting bullet/hero or not
     * @param other another FlyingObject
     * @return  a boolean value ,return true if one hit another
     */
    public boolean hit(FlyingObject other){
        int x1 = this.x - other.width;
        int x2 = this.x + this.width;
        int y1 = this.y - other.height;
        int y2 = this.y + this.height;
        int x = other.x;
        int y = other.y;

        return x>=x1 && x<=x2
                &&
                y>=y1 && y<=y2;
    }
}
