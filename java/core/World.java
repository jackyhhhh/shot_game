package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class World extends JPanel {
    private static final BufferedImage startImage;
    private static final BufferedImage pauseImage;
    private static final BufferedImage gameoverImage;
    static {
        startImage = FlyingObject.loadImage("start.png");
        pauseImage = FlyingObject.loadImage("pause.png");
        gameoverImage = FlyingObject.loadImage("gameover.png");
    }
    /**
     * the width of the world
     */
    public static final int WIDTH = 544;

    /**
     * the height of the world
     */
    public static final int HEIGHT = 929;

    /**
     * game status constants
     * Everything is already, the game is about to start
     */
    public static final int START = 0;

    /**
     * game status constants
     * the game is running
     */
    public static final int RUNNING = 1;

    /**
     * game status constants
     * the game is pause by player
     */
    public static final int PAUSE = 2 ;

    /**
     * game status constants
     * hero lose all of his lives, the game is over
     */
    public static final int GAME_OVER = 3;

    private int status = START;
    private int score = 0;
    private Sky sky = new Sky();
    private Hero hero = new Hero();
    private List<FlyingObject> enemies = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();

    /**
     * create the next enemy object
     * @return  an enemy of FlyingObject type
     */
    public FlyingObject nextOne(){
       Random rand = new Random();
       int type = rand.nextInt(20);
       if(type < 5){
           return new BigPlane();
       }else if(type < 18){
           return new AirPlane();
       }else{
           return new Bee();
       }
    }

    private int enterIndex = 0;
    /**
     * the enemy enter into the world
     */
    public void enterAction(){
        enterIndex++;
        if(enterIndex%40 == 0){
            FlyingObject obj = nextOne();
            enemies.add(obj);
        }
    }

    /**
     * all FlyObjects(including enemies and bullets) move a step
     */
    public void stepAction(){
        sky.step();
        for(FlyingObject f: enemies){
            f.step();
        }
        for(Bullet b: bullets){
            b.step();
        }
    }

    private int shootIndex = 0;
    /**
     * hero keep shooting bullets
     * bullets enter into the world
     */
    public void shootAction(){
        shootIndex++;
        if(shootIndex%30 == 0){
            List<Bullet> bs = hero.shoot();
            bullets.addAll(bs);
        }
    }

    /**
     * remove the FlyingObject who isOutOfBounds
     */
    public void outOfBoundsAction(){
        List<FlyingObject> enemiesAlive = new ArrayList<>();
        for(FlyingObject e : enemies){
            if(!e.outOfBounds() && !e.isRemove()){
                enemiesAlive.add(e);
            }
        }
        enemies = enemiesAlive;

        List<Bullet> bulletsAlive = new ArrayList<>();
        for(Bullet b : bullets){
            if(!b.outOfBounds() && !b.isRemove()){
                bulletsAlive.add(b);
            }
        }
        bullets = bulletsAlive;
    }

    /**
     * the bullet hit enemy event.
     * while hitting planes : get score
     * while hitting bees : get award (life or double fire)
     */
    public void bulletsHitEnemyAction(){
        for(FlyingObject f : enemies){
            for(Bullet b : bullets){
                if(f.isLife() && b.isLife() && f.hit(b)){
                    f.goDead();
                    b.goDead();
                    if(f instanceof Enemy){
                        score += ((Enemy) f).getScore();
                    }else if(f instanceof Award){
                        int type = ((Award) f).getType();
                        switch (type){
                            case Award.DOUBLE_FIRE:
                                hero.addDoubleFire();
                                break;
                            case Award.LIVES:
                                hero.addLife();
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * the event of hero hitting an enemy
     */
    public void heroHitEnemyAction(){
        for(FlyingObject f : enemies){
            if(f.isLife() && f.hit(hero)){
                f.goDead();
                hero.subtractLife();
                hero.clearDoubleFire();
            }
        }
    }

    /**
     * check whether the game is over or not
     */
    public void checkGameOverAction(){
        if(hero.getLife() <= 0){
            status = GAME_OVER;
        }
    }

    public void action(){
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(status == RUNNING){
                    int x = e.getX();
                    int y = e.getY();
                    hero.moveTo(x, y);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                switch (status){
                    case START:
                        status = RUNNING;
                        break;
                    case GAME_OVER:
                        score = 0;
                        sky = new Sky();
                        hero = new Hero();
                        enemies.clear();
                        bullets.clear();
                        status = START;
                        break;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(status == PAUSE){
                    status = RUNNING;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(status == RUNNING){
                    status = PAUSE;
                }
            }
        };
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(status == RUNNING){
                    enterAction();
                    stepAction();
                    shootAction();
                    outOfBoundsAction();
                    bulletsHitEnemyAction();
                    heroHitEnemyAction();
                    checkGameOverAction();
                }
                repaint();
            }
        },10, 10);
    }

    @Override
    public void paint(Graphics g) {
        sky.paintObject(g);
        hero.paintObject(g);
        for(FlyingObject f : enemies){
            f.paintObject(g);
        }
        for(Bullet b : bullets){
            b.paintObject(g);
        }
        g.drawString("SCORE: "+score, 10, 25);
        g.drawString("LIVES: "+hero.getLife(), 10, 45);

        switch(status){
            case START:
                g.drawImage(startImage, 0, 0, null);
                break;
            case PAUSE:
                g.drawImage(pauseImage, 0, 0, null);
                break;
            case GAME_OVER:
                g.drawImage(gameoverImage, 0, 0, null);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();        //create a window object
        World world = new World();          //create a panel object
        frame.add(world);                   //add panel into window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       //setting that closing window will quit the game
        frame.setSize(WIDTH, HEIGHT);       //set the window size according to the World width/height
        frame.setLocationRelativeTo(null);  //the window will be placed in the center of the screen
        frame.setVisible(true);             //1)set the window visible      2)invoke paint() method as soon as possible
        world.action();                     //start the game
    }

}
