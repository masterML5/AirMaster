/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.obj;

/**
 *
 * @author Milos
 */
public class ModelBoom {

    /**
     * @return the size
     */
    public double getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * @return the angle
     */
    public float getAngle() {
        return angle;
    }
    
    public ModelBoom(double size, float angle){
        this.size = size;
        this.angle = angle;
    }

    /**
     * @param angle the angle to set
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }
    private double size;
    private float angle;
}
