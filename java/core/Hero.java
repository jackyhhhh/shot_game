package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Hero extends FlyingObject {
    private static List<BufferedImage> images;
    static {
        images = new ArrayList<>();
        for(int i=0;i<6;i++){
            images.add(loadImages("hero"+i+".png"));
        }
    }

    private int life;
    private int doubleFire;
    Hero() {
        super(127, 169, 210, 550);
        life = 3;
        doubleFire = 0;
    }

    @Override
    public void step() {

    }

    private int deadIndex = 0;
    @Override
    public BufferedImage getImage() {
        if(isLife()){
            return images.get(0);
        }else if(isDead()){
            BufferedImage img = images.get(deadIndex++);
            if(deadIndex == images.size()){
                state = DEAD;
            }
            return img;
        }
        return null;
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    public void moveTo(int x, int y){
        this.x = x - this.width/2;
        this.y = y - this.height/2;
    }

    /**
     * hero shoot bullets:
     * if hero has double fire ,shoot two bullet by one time
     * if hero has no double fire, shoot one bullet by one time
     * @return an ArrayList of bullets
     */
    public List<Bullet> shoot(){
        int xStep = this.width/4;    // 四分之一英雄机的高
        int yStep = 20;
        List<Bullet> bs = new ArrayList<>();
        if(doubleFire > 0){
            bs.add(new Bullet(this.x + xStep, this.y - yStep));
            bs.add(new Bullet(this.x + 3*xStep, this.y - yStep));
        }else{
            bs.add(new Bullet(this.x + 2*xStep, this.y - yStep));
        }
        return bs;
    }

    /**
     * hero get awards by kill enemies
     * There ara two award type: 0 or 1
     * when awardType=0 ,hero get 1 life
     */
    public void addLife(){
        life++;
    }

    /**
     * hero get awards by kill enemies
     * There ara two award type: 0 or 1
     * when awardType=1, hero get 40 doubleFire
     */
    public void addDoubleFire(){
        doubleFire += 40;
    }

    /** get the runtime count of hero's life */
    public int getLife(){
        return life;
    }

    /**
     * hero lose one life when hitting the enemy or letting the enemy  go
     */
    public void subtractLife(){
        life--;
    }

    /**
     * hero's double fire will be cleared  right after losing life
     */
    public void clearDoubleFire(){
        doubleFire = 0;
    }

}
