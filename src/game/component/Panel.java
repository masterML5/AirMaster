/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.component;

import game.obj.Bullet;
import game.obj.Effect;
import game.obj.Player;
import game.obj.Rocket;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author Milos
 */
public class Panel extends JComponent {

    private static final String connectionUrlMySQL = "jdbc:mysql://localhost:3306/airmaster?user=root&password=";
    private static Connection conSQL;
    private int width;
    private int height;
    private Thread thread;
    private boolean start = true;
    private Graphics2D g2;
    private BufferedImage image;
    private Key key;
    private int shotTime;
    private String playerName;

    private final int FPS = 60;
    private final int TARGET_TIME = 1000000000 / FPS;

    private Player player;
    private List<Bullet> bullets;
    private List<Rocket> rockets;
    private List<Effect> boomEffects;
    private int score = 0;

    public void start(String playername) {
        try {
            conSQL = DriverManager.getConnection(connectionUrlMySQL);
            conSQL.setAutoCommit(false);
        } catch (SQLException ex) {
            System.out.println(ex);

        }
        width = getWidth();
        height = getHeight();
        this.playerName = playername;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    long startTime = System.nanoTime();
                    drawBackground();
                    drawGame();
                    render();
                    long time = System.nanoTime() - startTime;
                    if (time < TARGET_TIME) {
                        long sleep = (TARGET_TIME - time) / 1000000;
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

    private void initKeyboard() {
        key = new Key();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        key.setKey_left(true);
                        break;
                    case KeyEvent.VK_D:
                        key.setKey_right(true);
                        break;
                    case KeyEvent.VK_W:
                        key.setKey_space(true);
                        break;
                    case KeyEvent.VK_SPACE:
                        key.setKey_j(true);
                        break;
                    case KeyEvent.VK_CONTROL:
                        key.setKey_k(true);
                        break;
                    case KeyEvent.VK_F1:
                        key.setKey_f1(true);
                        break;
                    case KeyEvent.VK_F2:
                        key.setKey_f2(true);
                        break;
                    case KeyEvent.VK_F3:
                        key.setKey_f3(true);
                        break;
                    case KeyEvent.VK_F4:
                        key.setKey_f4(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        key.setKey_left(false);
                        break;
                    case KeyEvent.VK_D:
                        key.setKey_right(false);
                        break;
                    case KeyEvent.VK_W:
                        key.setKey_space(false);
                        break;
                    case KeyEvent.VK_SPACE:
                        key.setKey_j(false);
                        break;
                    case KeyEvent.VK_CONTROL:
                        key.setKey_k(false);
                        break;
                    case KeyEvent.VK_F1:
                        key.setKey_f1(false);
                        break;
                    case KeyEvent.VK_F2:
                        key.setKey_f2(false);
                        break;
                    case KeyEvent.VK_F3:
                        key.setKey_f3(false);
                        break;
                    case KeyEvent.VK_F4:
                        key.setKey_f4(false);
                        break;

                    default:
                        break;
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                float s = 0.5f;
                while (start) {
                    if (player.isAlive()) {
                        float angle = player.getAngle();
                        if (key.isKey_left()) {
                            angle -= s;
                        }
                        if (key.isKey_right()) {
                            angle += s;
                        }
                        if (key.isKey_j() || key.isKey_k()) {
                            if (shotTime == 0) {
                                if (key.isKey_j()) {
                                    bullets.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 5, 3f));
                                } else {
                                    bullets.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 20, 3f));
                                }
                            }
                            shotTime++;
                            if (shotTime == 15) {
                                shotTime = 0;
                            }
                        } else {
                            shotTime = 0;
                        }

                        if (key.isKey_space()) {
                            player.speedUp();
                        } else {
                            player.speedDown();
                        }
                        player.update();
                        player.changeAngle(angle);
                    } else {
                        if (key.isKey_f1()) {
                            resetGame();
                        } else if (key.isKey_f3()) {
                            try {
                                start = false;
                                String output = showScores();
                                int op = JOptionPane.showConfirmDialog(null, output, "Scores", JOptionPane.OK_CANCEL_OPTION);   
                               
                                if(op == 0){
                                    resetGame();
                                }else{
                                    exitGame();
                                }
                                 
                            } catch (SQLException ex) {
                                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                
                       
                                
                           
                        } else if (key.isKey_f2()) {
                            start = false;
                            try {
                                if(saveScores(playerName,Integer.valueOf(score))){
                                    int op = JOptionPane.showConfirmDialog(null, "Data recorded \n Start over?", "Scores", JOptionPane.OK_CANCEL_OPTION);  
                                    if(op == 0){
                                    resetGame();
                                }else{
                                    exitGame();
                                }
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                
                            
                            
                        } else if (key.isKey_f4()) {
                            exitGame();
                        }
                    }
                    for (int i = 0; i < rockets.size(); i++) {
                        Rocket rocket = rockets.get(i);
                        if (rocket != null) {
                            rocket.update();
                            if (!rocket.check(width, height)) {
                                rockets.remove(rocket);

                            } else {
                                if (player.isAlive()) {
                                    checkPlayer(rocket);
                                }
                            }

                        }
                    }
                    sleep(5);
                }
            }

        }).start();
    }
    public String showScores() throws SQLException {
    ArrayList msg = scores();
        String output = "Player scores \n";
    for (int i = 0; i < msg.size(); i++) {
        String message = msg.get(i).toString();
        output += message + "\n";
    }
        
    return output;
}
    public ArrayList scores() throws SQLException {
        String sqlScores = "SELECT * FROM scores order by score desc";
        PreparedStatement pstScores = conSQL.prepareStatement(sqlScores);
        ResultSet rsScores = pstScores.executeQuery();
        ArrayList scores = new ArrayList();
        int i = 1;
        while(rsScores.next()){
            
            scores.add(i +" • "+ rsScores.getString("player")+" • "+rsScores.getInt("score"));
            i++;
        }
    
        return scores;

    }

    private boolean saveScores(String playername, int score) throws SQLException {
        String sqlInsertScore = "INSERT INTO scores(player,score) VALUES (?,?)";
        PreparedStatement pstInsertScore = conSQL.prepareStatement(sqlInsertScore);
        pstInsertScore.setString(1, playername);
        pstInsertScore.setInt(2, score);
        pstInsertScore.addBatch();
        int insert = pstInsertScore.executeUpdate();
        conSQL.commit();
        return insert > 0;
    }

    private void exitGame() {
        start = false;
        System.exit(0);
    }

    private void initBullet() {
        bullets = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    for (int i = 0; i < bullets.size(); i++) {
                        Bullet bullet = bullets.get(i);
                        if (bullet != null) {
                            bullet.update();
                            checkBullets(bullet);
                            if (!bullet.check(width, height)) {
                                bullets.remove(bullet);
                            }
                        } else {
                            bullets.remove(bullet);
                        }
                    }
                    for (int i = 0; i < boomEffects.size(); i++) {
                        Effect boomEffect = boomEffects.get(i);
                        if (boomEffect != null) {
                            boomEffect.update();
                            if (!boomEffect.check()) {
                                boomEffects.remove(boomEffect);
                            }
                        } else {
                            boomEffects.remove(boomEffect);
                        }
                    }
                    sleep(1);
                }
            }
        }).start();

    }

    private void addRocket() {
        Random ran = new Random();
        int locationY = ran.nextInt(height - 50) + 25;
        Rocket rocket = new Rocket();
        rocket.changeLocation(0, locationY);
        rocket.changeAngle(0);
        rockets.add(rocket);
        int locationY2 = ran.nextInt(height - 50) + 25;
        Rocket rocket2 = new Rocket();
        rocket2.changeAngle(180);
        rocket2.changeLocation(width, locationY2);
        rockets.add(rocket2);
    }

    private void initObjectGame() {
        player = new Player();
        player.changeLocation(150, 150);
        rockets = new ArrayList<>();
        boomEffects = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    addRocket();
                    sleep(3000);
                }
            }
        }).start();

    }

    private void checkBullets(Bullet bullet) {
        for (int i = 0; i < rockets.size(); i++) {
            Rocket rocket = rockets.get(i);
            if (rocket != null) {
                Area area = new Area(bullet.getShape());
                area.intersect(rocket.getShape());
                if (!area.isEmpty()) {
                    boomEffects.add(new Effect(bullet.getCenterX(), bullet.getCenterY(), 3, 5, 60, 0.5f, new Color(230, 207, 105)));
                    if (!rocket.updateHP(bullet.getSize())) {
                        score++;
                        rockets.remove(rocket);
                        double x = rocket.getX() + Rocket.ROCKET_SIZE / 2;
                        double y = rocket.getY() + Rocket.ROCKET_SIZE / 2;
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                        boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                        boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                        boomEffects.add(new Effect(x, y, 5, 5, 150, 0.2f, new Color(255, 255, 255)));

                    }
                    //rockets.remove(rocket);
                    bullets.remove(bullet);
                }
            }

        }
    }

    private void checkPlayer(Rocket rocket) {

        if (rocket != null) {
            Area area = new Area(player.getShape());
            area.intersect(rocket.getShape());
            if (!area.isEmpty()) {
                double rocketHp = rocket.getHP();
                if (!rocket.updateHP(player.getHP())) {
                    rockets.remove(rocket);
                    double x = rocket.getX() + Rocket.ROCKET_SIZE / 2;
                    double y = rocket.getY() + Rocket.ROCKET_SIZE / 2;
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                    boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                    boomEffects.add(new Effect(x, y, 5, 5, 150, 0.2f, new Color(255, 255, 255)));

                }
                if (!player.updateHP(rocketHp)) {
                    player.sertAlive(false);
                    double x = player.getX() + Player.PLAYER_SIZE / 2;
                    double y = player.getY() + Player.PLAYER_SIZE / 2;
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                    boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                    boomEffects.add(new Effect(x, y, 5, 5, 150, 0.2f, new Color(255, 255, 255)));

                }

            }

        }
    }

    private void drawBackground() {
        g2.setColor(new Color(30, 30, 30));
        g2.fillRect(0, 0, width, height);
    }

    private void drawGame() {
        if (player.isAlive()) {
            player.draw(g2);
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet != null) {
                bullet.draw(g2);
            }
        }
        for (int i = 0; i < rockets.size(); i++) {
            Rocket rocket = rockets.get(i);
            if (rocket != null) {
                rocket.draw(g2);
            }
        }
        for (int i = 0; i < boomEffects.size(); i++) {
            Effect boomEffect = boomEffects.get(i);
            if (boomEffect != null) {
                boomEffect.draw(g2);
            }
        }
        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
        g2.drawString("Score : " + score, 10, 20);
        g2.drawString("Player : " + playerName, 1220, 20);
        if (!player.isAlive()) {
            String text = "GAME OVER";
            String textKey = "Choose options to resume : New Game - F1, Save Score - F2, View Scores - F3, Exit - F4";
            String playerScore = "PLAYER : " + playerName + "    SCORE : " + String.valueOf(score);

            g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r2 = fm.getStringBounds(text, g2);
            double textWidth = r2.getWidth();
            double textHeight = r2.getHeight();
            double x = (width - textWidth) / 2;
            double y = (height - textHeight) / 2;
            g2.drawString(text, (int) x, (int) y + fm.getAscent());

            g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
            fm = g2.getFontMetrics();
            r2 = fm.getStringBounds(playerScore, g2);
            textWidth = r2.getWidth();
            textHeight = r2.getHeight();
            x = (width - textWidth) / 2;
            y = (height - textHeight) / 2;
            g2.drawString(playerScore, (int) x, (int) y + fm.getAscent() + 50);

            g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
            fm = g2.getFontMetrics();
            r2 = fm.getStringBounds(textKey, g2);
            textWidth = r2.getWidth();
            textHeight = r2.getHeight();
            x = (width - textWidth) / 2;
            y = (height - textHeight) / 2;
            g2.drawString(textKey, (int) x, (int) y + fm.getAscent() + 100);

        }

    }

    public void resetGame() {
        score = 0;
        rockets.clear();
        bullets.clear();
        player.changeLocation(150, 150);
        player.reset();
        
        thread.stop();
        start(playerName);
        start = true;
        
        

    }

    private void render() {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    private void sleep(long speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}
