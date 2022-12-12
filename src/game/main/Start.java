/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.main;

import game.component.Panel;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * 
 * @author Milos
 */


public class Start extends JFrame{
    String playerName;
    public Start(String player) throws IOException{
        playerName = player;
init();
}
    public void init() throws IOException{
        setTitle("Air Master");
        setSize(1200,650);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Panel panel = new Panel();
        add(panel);
        addWindowListener(new WindowAdapter(){
        @Override
        public void windowOpened(WindowEvent e){
            try {
                panel.start(playerName);
            } catch (IOException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });
    }
    public static void main(String[] args){
    
    }
}
