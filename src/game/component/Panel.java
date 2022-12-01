/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.component;

import game.obj.Bullet;
import game.obj.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author Milos
 */
public class Panel extends JComponent {
    private int width;
    private int height;
    private Thread thread;
    private boolean start = true;
    private Graphics2D g2;
    private BufferedImage image;
    private Key key;
    private int shotTime;
    
    
    private final int FPS = 60;
    private final int TARGET_TIME = 1000000000 / FPS;
    
    private Player player;
    private List<Bullet> bullets;
    
    public void start(){
        width=getWidth();
        height= getHeight();
        image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        thread = new Thread(new Runnable(){
            @Override
            public void run(){
                while(start){
                    long startTime = System.nanoTime();
                    drawBackground();
                    drawGame();
                    render();
                    long time = System.nanoTime() - startTime;
                    if(time<TARGET_TIME){
                        long sleep = (TARGET_TIME-time)/1000000;
                        sleep(sleep);
                        
                    }
                }
            }
        });
        initObjectGame();
        initKeyboard();
        initBullet();
        thread.start();
    }
    private void initKeyboard(){
        key = new Key();
        requestFocus();
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        key.setKey_left(true);
                        break;
                    case KeyEvent.VK_DOWN:
                        key.setKey_right(true);
                        break;
                    case KeyEvent.VK_SPACE:
                        key.setKey_space(true);
                        break;
                    case KeyEvent.VK_LEFT:
                        key.setKey_j(true);
                        break;
                    case KeyEvent.VK_RIGHT:
                        key.setKey_k(true);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e){
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        key.setKey_left(false);
                        break;
                    case KeyEvent.VK_DOWN:
                        key.setKey_right(false);
                        break;
                    case KeyEvent.VK_SPACE:
                        key.setKey_space(false);
                        break;
                    case KeyEvent.VK_LEFT:
                        key.setKey_j(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        key.setKey_k(false);
                        break;
                    default:
                        break;
                }
            }
        });
        new Thread(new Runnable(){
            @Override
            public void run(){
                float s = 0.5f;
                while(start){
                    float angle = player.getAngle();
                    if(key.isKey_left()){
                        angle -=s;
                    }
                    if(key.isKey_right()){
                        angle +=s;
                    }
                    
                    if(key.isKey_space()){
                        player.speedUp();
                    }else{
                        player.speedDown();
                    }
                    player.update();
                    player.changeAngle(angle);
                    sleep(5);
                }
            }
        }).start();
    }
    private void initBullet(){
        bullets = new ArrayList<>();
        new Thread(new Runnable(){
        @Override
        public void run(){
        while(start){
        for(int i=0; i<bullets.size(); i++){
        Bullet bullet = bullets.get(i);
        if(bullet!=null){
            bullet.update();
            if(!bullet.check(width, height)){
                bullets.remove(bullet);
            }
        }else{
            bullets.remove(bullet);
        }
    }sleep(1);
    }
    }
    }).start();
       
    }
    private void initObjectGame(){
        player = new Player();
        player.changeLocation(150, 150);
    }
    private void drawBackground(){
        g2.setColor(new Color(30,30,30));
        g2.fillRect(0, 0, width, height);
    }
    private void drawGame(){
        player.draw(g2);
    }
    private void render(){
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }
    private void sleep(long speed){
        try{
        Thread.sleep(speed);
    } catch(InterruptedException ex){
            System.out.println(ex);
    }
    }
}
