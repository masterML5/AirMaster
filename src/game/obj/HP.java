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
public class HP {

    /**
     * @return the MAX_HP
     */
    public double getMAX_HP() {
        return MAX_HP;
    }

    /**
     * @param MAX_HP the MAX_HP to set
     */
    public void setMAX_HP(double MAX_HP) {
        this.MAX_HP = MAX_HP;
    }

    /**
     * @return the currentHp
     */
    public double getCurrentHp() {
        return currentHp;
    }

    /**
     * @param currentHp the currentHp to set
     */
    public void setCurrentHp(double currentHp) {
        this.currentHp = currentHp;
    }
    public HP(double MAX_HP, double currentHp){
        this.MAX_HP = MAX_HP;
        this.currentHp = currentHp;
    }
    
    public HP(){
        
    }
    private double MAX_HP;
    private double currentHp;
}
