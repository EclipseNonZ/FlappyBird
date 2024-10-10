import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener,KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    Image backgoundImg;
    Image bottomPipeImg;
    Image topPipeImg;
    Image birdImg;

    //Bird
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    //Pipe
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img; 
        }
    }

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

    //Game logic
    Bird bird;

    boolean gameOver = false;

    Timer gameLoop;
    Timer createPipeTimer;

    double score = 0;

    int velocityX = -4;
    int velocityY = -9;
    int gravity = 1;

    ArrayList<Pipe> pipes;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setFocusable(true);
        addKeyListener(this);

        //Load Image
        backgoundImg = new ImageIcon(getClass().getResource("./images/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./images/flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./images/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./images/bottompipe.png")).getImage();
    
        //create Bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //place pipes Timer
        createPipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e ){
                createPipe();
            }
        });
        createPipeTimer.start();
        //game Timer
        gameLoop = new Timer(1000/60, this); // 60FPS
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //System.out.println("update"); //Testing for GameLoop
        //Draw Backgound
        g.drawImage(backgoundImg, 0, 0, boardWidth,boardHeight,null);


        //Draw Bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //Draw Pipe
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over!! : "+ String.valueOf((int)score), boardWidth/5, boardHeight/2);
        }else{
            g.drawString(String.valueOf((int)score), 10, 35);
        }
    }

    public void createPipe(){
        //randomFrom Math.random = 0-1
        //pipeHeight/4 = 128
        //0 - 128 - (0 -> 256)
        //position Y --> 1/4 pipeHight to 3/4 pipeHeight
        int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight / 4;
        
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void move(){
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y,0);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x+pipeWidth){
                pipe.passed =true;
                score += 0.5;
            }


            if (Collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    public boolean Collision(Bird a , Pipe b){
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        move();
        repaint();
        if (gameOver) {
            createPipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameOver) {
                gameOver = false;
                createPipeTimer.start();
                gameLoop.start();
                pipes.clear();
                score = 0;
                bird.y = birdY;
            }
            velocityY = -9;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }
}
