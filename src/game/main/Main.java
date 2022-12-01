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
import javax.swing.JFrame;

/**
 *
 * 
 * @author Milos
 */


public class Main extends JFrame{
    public Main(){
init();
}
    private void init(){
        setTitle("Air Master");
        setSize(1366,768);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Panel panel = new Panel();
        add(panel);
        addWindowListener(new WindowAdapter(){
        @Override
        public void windowOpened(WindowEvent e){
            panel.start();
        }
    });
    }
    public static void main(String[] args){
    Main main = new Main();
    main.setVisible(true);
    }
}
