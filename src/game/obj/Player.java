/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.ImageIcon;

/**
 *
 * @author Milos
 */
public class Player extends HpRender{

    public Player() {
        super(new HP(50,50));
        this.image = new ImageIcon(getClass().getResource("/game/image/plane.png")).getImage();
        
        this.image_speed = new ImageIcon(getClass().getResource("/game/image/plane-move.png")).getImage();
        Path2D p = new Path2D.Double();
        p.moveTo(0, 15);
        p.lineTo(25,5);
        p.lineTo(PLAYER_SIZE +15,PLAYER_SIZE/2);
        
        p.lineTo(35,PLAYER_SIZE-5);       
        p.lineTo(0, PLAYER_SIZE - 15);
        playerShap = new Area(p);
    }

    public static final double PLAYER_SIZE = 64;
    private double x;
    private double y;
    private  float MAX_SPEED = 1f;
    private float speed = 0f;
    private float angle = 0f;
    private final Image image;
    private final Image image_speed;
    private boolean speedUp;
    private final Area playerShap;
    private boolean alive=true;
    
    public void changeLocation(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public void changeAngle(float angle){
        if(angle<0){
            angle=359;
        }else if(angle>359){
            angle=0;
        }
        this.angle=angle;
    }
    
    public void update(){
        x+=Math.cos(Math.toRadians(angle))*speed;
        y+=Math.sin(Math.toRadians(angle))*speed;
    }
    public void draw(Graphics2D g2){
        AffineTransform oldTransform = g2.getTransform();
        g2.translate(x, y);
        AffineTransform tran = new AffineTransform();
        tran.rotate(Math.toRadians(angle+45),PLAYER_SIZE /2 , PLAYER_SIZE /2);
        g2.drawImage(speedUp ? image_speed : image, tran,null);
        hpRender(g2,getShape(),y);
        g2.setTransform(oldTransform);
        g2.setColor(new Color(15,123,123));
        g2.draw(getShape().getBounds());
        g2.draw(getShape());
    }
    
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public float getAngle(){
        return angle;
    }
    
    public void speedUp(){
        speedUp = true;
        if(speed>MAX_SPEED){
            speed = MAX_SPEED;
        }else{
            speed += 0.01f;
        }
    }
    public void speedDown(){
        speedUp = false;
        if(speed<=0){
            speed = 0;
        }else{
            speed -= 0.03f;
        }
    }
     public Area getShape() {
        AffineTransform afx = new AffineTransform();
        afx.translate(x, y);
        afx.rotate(Math.toRadians(angle), PLAYER_SIZE / 2, PLAYER_SIZE / 2);
        return new Area(afx.createTransformedShape(playerShap));
    }
     
     public boolean isAlive(){
         
         return alive;
     }
     public void setAlive(boolean alive){
         this.alive=alive;
     }
     public void reset(){
         alive = true;
         resetHP();
         angle = 0;
         speed = 0;
     }
     
     public void checkPosition(int width, int height) {
        Rectangle size = getShape().getBounds();
        if (x <= -size.getWidth() || x > width ) {
           changeLocation(x, getY());
        } else if(y < -size.getHeight() || y > height){
            changeLocation(getX(), y);
        }

    }
    
}
