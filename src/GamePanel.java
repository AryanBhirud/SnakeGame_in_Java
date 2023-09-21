import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    static final int ScreenWidth = 600;
    static final int ScreenHeight = 600;
    static final int UnitSize = 25;
    static final int GameUnits = (ScreenHeight*ScreenWidth)/UnitSize;
    int Delay = 100;
    final int x[] = new int[GameUnits];
    final int y[] = new int[GameUnits];
    int SnakeLength = 6;
    int ApplesEaten = 0;
    int AppleX, AppleY;
    char Direction = 'R';
    boolean Running = false;
    Timer timer;
    Random random;
    GamePanel () {
        random = new Random();
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();;
    }
    public void startGame(){
        createApple();
        Running = true;
        timer = new Timer(Delay, this);
        timer.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(Running){
            for (int i= 0; i < ScreenHeight/UnitSize; i++) {
                g.drawLine(i*UnitSize, 0, i*UnitSize, ScreenHeight);
                g.drawLine(0, i*UnitSize, ScreenWidth, i*UnitSize);
            }
            g.setColor(Color.red);
            g.fillOval(AppleX, AppleY, UnitSize, UnitSize);
            for(int i = 0; i<SnakeLength; i++){
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[0], y[0], UnitSize, UnitSize);
                }else{
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UnitSize, UnitSize);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+ApplesEaten, (ScreenWidth - metrics.stringWidth("Score: "+ApplesEaten))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }
    public void createApple() {
        AppleX = random.nextInt(ScreenWidth/UnitSize)*UnitSize;
        AppleY = random.nextInt(ScreenHeight/UnitSize)*UnitSize;
    }
    public void move(){
        for(int i = SnakeLength; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (Direction) {
            case 'U':
                y[0] -= UnitSize;
                break;
        
            case 'D':
                y[0] += UnitSize;
                break;
            case 'R':
                x[0] += UnitSize;
                break;
            case 'L':
                x[0] -= UnitSize;
                break;
        }
    }
    public void checkApple(){
        if((x[0] == AppleX) && (y[0] == AppleY)){
            ApplesEaten++;
            SnakeLength++;
            createApple();
        }
    }
    public void checkCollision(){
        for(int i = SnakeLength; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                Running = false;
            }
        }
        if((x[0] < 0) || (x[0] > ScreenWidth) || (y[0] < 0) || (y[0] > ScreenHeight)){
            Running = false;
        }
        if(Running == false){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (ScreenWidth - metrics.stringWidth("Game Over"))/2, ScreenHeight/2);
        g.setFont(new Font("Ink Free", Font.PLAIN, 40));
        g.drawString("Score: "+ApplesEaten, (ScreenWidth - metrics.stringWidth("Game Over"))/2, (ScreenHeight/2)+15+g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(Running){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
        
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(Direction != 'R'){
                        Direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(Direction != 'L'){
                        Direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(Direction != 'D'){
                        Direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(Direction != 'U'){
                        Direction = 'D';
                    }
                    break;
            
            }
        }
    }
    
}
