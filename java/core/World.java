package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.awt.Graphics;

public class World extends JPanel implements Serializable {
    private static final int serialVersionUID = 2;
    private static final Map<String, BufferedImage> images = new HashMap<>();
    static {
        images.put("start", FlyingObject.loadImage("images"+ File.separator+"start.png"));
        images.put("pause", FlyingObject.loadImage("images"+ File.separator+"pause.png"));
        images.put("over", FlyingObject.loadImage("images"+ File.separator+"over.png"));
        images.put("win", FlyingObject.loadImage("images"+ File.separator+"win.png"));
        images.put("saveButton", FlyingObject.loadImage("images"+ File.separator+"saveButton.png"));
        images.put("saving", FlyingObject.loadImage("images"+ File.separator+"saving.png"));
        images.put("loading", FlyingObject.loadImage("images"+ File.separator+"loading.png"));
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
    public static final int WIN = 4;
    public static final int SAVING = 5;
    public static final int LOADING = 6;


    private int gameStatus;
    private int score;
    private int level;
    private int actionIndex;
    private Sky sky;
    private Hero hero;
    private List<FlyingObject> enemies;
    private List<Bullet> bullets;

    World(){
        initGame();
    }

    private void initGame(){
        System.out.println("正在初始化游戏...");
        gameStatus = START;
        score = 0;
        level = 1;
        actionIndex = 0;
        sky = new Sky();
        hero = new Hero();
        enemies = Collections.synchronizedList(new ArrayList<>());
        bullets = Collections.synchronizedList(new ArrayList<>());
        System.out.println("游戏初始化成功, 准备就绪!");
    }

    private void saveGame(){
        System.out.println("正在保存当前游戏状态...");
        System.out.println(" score:"+score+
                "\n level: "+level
                +"\n hero_health: "+hero.getHealth()
                +"\n enemies_count: "+enemies.size()
                +"\n bullets_count: "+bullets.size()
        );
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(
                            new File("profile"+File.separator+"save_file.dat"
                            )
                    )
            );

            oos.writeObject(score+"-"+level);
            oos.writeObject(sky);
            oos.writeObject(hero);
            oos.writeObject(enemies);
            oos.writeObject(bullets);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("游戏存档成功!!");
    }

    @SuppressWarnings("unchecked")
    private void loadGame(){
        System.out.println("正在加载上次存档的游戏...");
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(
                            new File("profile"+File.separator+"save_file.dat"
                            )
                    )
            );
            actionIndex = 0;
            String line = (String) ois.readObject();
            String[] data = line.split("-");
            score = Integer.parseInt(data[0]);
            level = Integer.parseInt(data[1]);
            sky = (Sky) ois.readObject();
            hero = (Hero) ois.readObject();
            enemies = (List<FlyingObject>) ois.readObject();
            bullets = (List<Bullet>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(" score:"+score+
                "\n level: "+level
                +"\n hero_health: "+hero.getHealth()
                +"\n enemies_count: "+enemies.size()
                +"\n bullets_count: "+bullets.size()
        );
        System.out.println("加载成功!!!");
    }

    /**
     * create the next enemy object
     * @return  an enemy of FlyingObject type
     */
    public FlyingObject nextOne(){
       FlyingObject f;
       Random rand = new Random();
       int type = rand.nextInt(20);
       if(type < 5){
           f = new BigPlane();
       }else if(type < 18){
           f = new AirPlane();
       }else{
           f = new Bee();
       }
       ((Enemy) f).setSpeedByLevel(level);
       return f;
    }

    /**
     * the enemy enter into the world
     */
    public void enterAction(){
        if(actionIndex % 40 == 0){
            enemies.add(nextOne());
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

    /**
     * hero keep shooting bullets
     * bullets enter into the world
     */
    public void shootAction(){
        if(actionIndex % 10 == 0){
            List<Bullet> bs = hero.shoot(level);
            bullets.addAll(bs);
        }
    }

    /**
     * remove the FlyingObject who isOutOfBounds
     */
    public void outOfBoundsAction(){
        List<FlyingObject> enemiesDead = new ArrayList<>();
        for(FlyingObject e : enemies){
            if(e.outOfBounds() || e.isRemove()){
                enemiesDead.add(e);
            }
        }
        List<Bullet> bulletsDead = new ArrayList<>();
        for(Bullet b : bullets){
            if(b.outOfBounds() || b.isRemove()){
                bulletsDead.add(b);
            }
        }

        synchronized (this){
            enemies.removeAll(enemiesDead);
            bullets.removeAll(bulletsDead);
        }
    }

    /**
     * the bullet hit enemy event.
     * while hitting planes : get score
     * while hitting bees : get award (life or double fire)
     */
    public void bulletsHitEnemyAction(){
        for(FlyingObject f : enemies){
            for(Bullet b : bullets){
                if(f.isAlive() && b.isAlive() && f.hit(b)){
                    b.goDead();
                    f.subtractHealth();
                    if(f.isDead()){
                        int type = ((Enemy)f).getAwardType();
                        switch (type){
                            case Enemy.DOUBLE_FIRE:
                                hero.addDoubleFire();
                                break;
                            case Enemy.HEALTH:
                                hero.addHealth();
                                break;
                            case Enemy.SCORE:
                                score += ((Enemy) f).getScore();
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
            if(f.isAlive() && f.hit(hero)){
                f.goDead();
                hero.subtractHealth();
                hero.clearDoubleFire();
            }
        }
    }

    /**
     * check whether the game is over or not
     */
    public void checkGameOverAction(){
        if(hero.getHealth() <= 0){
            gameStatus = GAME_OVER;
        }
    }

    public void checkGameLevelAction(){
        if(score <= 10){
            level = 1;
        }else if(score <= 30){
            level = 2;
        }else if(score <= 60){
            level = 3;
        }else if(score <= 120){
            level = 4;
        }else if(score <= 150){
            level = 5;
        }else{
            gameStatus = WIN;
        }
    }

    public void action(){
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(gameStatus == RUNNING){
                    int x = e.getX();
                    int y = e.getY();
                    hero.moveTo(x, y);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                switch (gameStatus){
                    case START:
                        gameStatus = RUNNING;
                        break;
                    case GAME_OVER:
                        initGame();
                        break;
                    case RUNNING:
                        gameStatus = PAUSE;
                        break;
                    case PAUSE:
                        int x = e.getX();
                        int y = e.getY();
                        if(y>=0 && y<=39){
                            if(x>=WIDTH-144 && x<WIDTH-71){
                                gameStatus = SAVING;
                                saveGame();
                                gameStatus = PAUSE;
                                break;
                            }else if(x>=WIDTH-71 && x<=WIDTH){
                                gameStatus = LOADING;
                                loadGame();
                                gameStatus = PAUSE;
                                break;
                            }
                        }else {
                            gameStatus = RUNNING;
                            break;
                        }
                    case WIN:
                        initGame();
                        gameStatus = START;
                        break;
                }
            }
        };
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(gameStatus == RUNNING){
                    actionIndex++;
                    enterAction();
                    stepAction();
                    shootAction();
                    outOfBoundsAction();
                    bulletsHitEnemyAction();
                    heroHitEnemyAction();
                    checkGameOverAction();
                    checkGameLevelAction();
                }
                repaint();
            }
        },10, 10);
    }

    @Override
    public void paint(Graphics g) {
        sky.paintObject(g);
        hero.paintObject(g);
        synchronized (this){
            for(FlyingObject f : enemies){
                f.paintObject(g);
            }
            for(Bullet b : bullets){
                b.paintObject(g);
            }
        }

        switch(gameStatus){
            case START:
                g.drawImage(images.get("start"), 0, 0, null);
                break;
            case PAUSE:
                g.drawImage(images.get("saveButton"), 0, 0, null);
                g.drawImage(images.get("pause"), 0, 0, null);
                break;
            case GAME_OVER:
                g.drawImage(images.get("over"), 0, 0, null);
                break;
            case WIN:
                g.drawImage(sky.getImage(), 0, 0, null);
                g.drawImage(images.get("win"), 0, 0, null);
                break;
            case SAVING:
                g.drawImage(sky.getImage(), 0, 0, null);
                g.drawImage(images.get("saving"), 0, 0, null);
                break;
            case LOADING:
                g.drawImage(sky.getImage(), 0, 0, null);
                g.drawImage(images.get("loading"), 0, 0, null);
                break;
        }

        g.drawString("SCORE: "+score, 10, 25);
        g.drawString("HEALTH: "+hero.getHealth(), 10, 45);
        g.drawString("LEVEL: "+level, 10, 65);
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
