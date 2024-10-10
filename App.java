import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.setVisible(true);
        window.setResizable(false);
        window.setSize(boardWidth,boardHeight);
        window.setLocationRelativeTo(null);
        window.setTitle("FlappyBirdByNonZ");
        FlappyBird flappyBird = new FlappyBird();
        window.add(flappyBird);
        window.pack();
        flappyBird.requestFocus();
        window.setVisible(true);
    }
}
