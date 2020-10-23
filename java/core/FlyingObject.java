package core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

public abstract class FlyingObject implements Serializable {
    private static final int serialVersionUID = 1;
    protected int width;
    protected int height;
    protected int x, xSpeed;
    protected int y, ySpeed;
    protected int status = ALIVE;
    protected int deadIndex = 0;        // 敌机死了时, 图片轮播开始的下标(爆炸效果)
    protected int health = 1;

    /**
     * state constants of the flying object
     * Represents that the flyingObject is alive
     */
    public static final int ALIVE = 0;

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
        ySpeed = 2;
        xSpeed = 0;
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
        ySpeed = 2;
        xSpeed = 0;
    }

    /**
     * load image from disk(static resource)
     * @param pathname the name of the image
     * @return a BufferedImage type image
     */
    public static BufferedImage loadImage(String pathname){
        try {
            return ImageIO.read(new File(pathname));
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
    public boolean isAlive(){
        return status == ALIVE;
    }

    /**
     * judge whether the obj is dead or not
     * @return if dead return true, if not dead return false
     */
    public boolean isDead(){
        return status == DEAD;
    }

    /**
     * judge whether the obj can be removed or not
     * @return if can be removed return true, if can not be removed return false
     */
    public boolean isRemove(){
        return status == REMOVE;
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
        status = DEAD;
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

    public void addHealth(){
        health++;
    }

    public void subtractHealth(){
        health--;
        if(health <= 0){
            status = DEAD;
        }
    }

    public int getHealth(){
        return health;
    }
}
