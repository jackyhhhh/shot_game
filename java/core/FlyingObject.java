package core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public abstract class FlyingObject {
    public static final int LIFE = 0;
    public static final int DEAD = 1;
    public static final int REMOVE = 2;

    protected int state = LIFE;
    protected int width;
    protected int height;
    protected int x;
    protected int y;

    /** 固定x坐标出现的飞行物 */
    FlyingObject(int width, int height, int x, int y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    /** 随机x坐标出现的飞行物 */
    FlyingObject(int width, int height){
        this.width = width;
        this.height = height;
        Random rand = new Random();
        this.x = rand.nextInt(World.WIDTH - this.width);
        this.y = -this.height;
    }

    /** 加载图片(静态资源) */
    public static BufferedImage loadImages(String fileName){
        try {
            return ImageIO.read(FlyingObject.class.getResource(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /** 飞行物的移动 */
    public abstract void step();

    /** 获取实时图片 */
    public abstract  BufferedImage getImage();

    /** 绘画对象 g:画笔 */
    public void paintObject(Graphics g){
        g.drawImage(getImage(), x, y, null);
    }

    /** 判断是否活着 */
    public boolean isLife(){
        return state == LIFE;
    }

    /** 判断是否死了 */
    public boolean isDead(){
        return state == DEAD;
    }

    /** 判断是否可移除 */
    public boolean isRemove(){
        return state == REMOVE;
    }

    /** 判断飞行物是否越界 */
    public abstract boolean outOfBounds();

    /** 转变飞行物状态为DEAD */
    public void goDead(){
        state = DEAD;
    }
}
