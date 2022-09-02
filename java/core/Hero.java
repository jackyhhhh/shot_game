package core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Hero extends FlyingObject {
    private static final List<BufferedImage> images;
    static {
        images = new ArrayList<>();
        for(int i=0;i<6;i++){
            images.add(loadImage("images"+ File.separator+"hero"+i+".png"));
        }
    }

    private int doubleFire;
    Hero() {
        super(127, 169, 210, 550);
        health = 3;
        doubleFire = 0;
    }

    @Override
    public void step() {

    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    public void moveTo(int x, int y){
        this.x = x - this.width/2;
        this.y = y - this.height/2;
    }

    private int aliveIndex = 0;
    @Override
    public BufferedImage getImage(){
        if(isAlive()){
            return images.get(aliveIndex++ % 2);
        }else if(isDead()){
            BufferedImage img = images.get(deadIndex++);
            if(deadIndex == images.size()){
                status = REMOVE;
            }
            return img;
        }
        return null;
    }

    /**
     * hero shoot bullets:
     * if hero has double fire ,shoot two bullet by one time
     * if hero has no double fire, shoot one bullet by one time
     * @param level the game level
     * @return an ArrayList of bullets
     */
    public List<Bullet> shoot(int level){
        int xStep = this.width/4;    // 四分之一英雄机的宽
        int yStep = 20;
        List<Bullet> bs = new ArrayList<>();
        if(doubleFire > 0){
            bs.add(new Bullet(this.x + xStep, this.y - yStep));
            bs.add(new Bullet(this.x + 3*xStep, this.y - yStep));
        }else{
            bs.add(new Bullet(this.x + 2*xStep, this.y - yStep));
        }
        bs.forEach(bullet -> bullet.setSpeedByLevel(level));
        return bs;
    }

    /**
     * hero get awards by kill enemies
     * There ara two award type: 0 or 1
     * when awardType=1, hero get 40 doubleFire
     */
    public void addDoubleFire(){
        doubleFire += 40;
    }

    /**
     * hero's double fire will be cleared  right after losing life
     */
    public void clearDoubleFire(){
        doubleFire = 0;
    }

}
