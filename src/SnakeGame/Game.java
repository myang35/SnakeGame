package SnakeGame;

//import BookClasses.FileChooser;
import SnakeGame.PowerUps.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.Random;

/**
 *
 * @author marvi
 */
public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1550691097823471818L;

    public static final int WIDTH = 640, HEIGHT = WIDTH;

    private Thread thread;
    private boolean running = false;
    private int numTicks;
    public static ScoresHandler scoresHandler;
    public static int points;

    private Random r = new Random();
    private Handler handler;
    public static Head head;
    public static Food food;

    public static boolean paused;
    public static boolean gameOver;

    public Game() {
        String scoresPath = System.getProperty("user.dir") + "\\src\\SnakeGame\\scores.txt";
        scoresHandler = new ScoresHandler(new File(scoresPath));

        handler = new Handler();

        paused = false;
        gameOver = false;

        head = new Head(WIDTH / 2, HEIGHT / 2, handler);
        handler.addObject(head);

        food = new Food(r.nextInt(WIDTH - 22), r.nextInt(HEIGHT - 54), handler);
        handler.addObject(food);

        this.addKeyListener(new KeyInput(handler));

        new Window(WIDTH, HEIGHT, "Snake Game", this);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running) {
                render();
            }
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        if (!paused) {
            handler.tick();
            //createPowerUp();
        }
        
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // show score
        g.setFont(new Font("ARIEL", 0, 20));
        g.setColor(Color.WHITE);
        g.drawString("points: " + points, 10, 25);

        // show High Score
        try {
            g.drawString("HighScore: " + scoresHandler.getHighScore().getPoints(), WIDTH - 200, 25);
        } catch (IllegalStateException e) {
        }

        // pause screen
        if (paused) {
            g.setFont(new Font("ARIEL", 0, 50));
            g.drawString("PAUSED", WIDTH / 2 - 100, HEIGHT / 2 - 30);
            g.setFont(new Font("ARIEL", 0, 25));
            g.drawString("Press P to continue", WIDTH / 2 - 110, HEIGHT / 2 + 30);
        }

        // if head stops moving, game over
        if (head.getVelocity() == 0) {
            gameOver = true;
            g.setFont(new Font("ARIEL", 0, 50));
            g.drawString("GAME OVER", WIDTH / 2 - 150, HEIGHT / 2 - 30);
            g.setFont(new Font("ARIEL", 0, 25));
            g.drawString("Press ENTER to play again", WIDTH / 2 - 150, HEIGHT / 2 + 30);
            g.setFont(new Font("ARIEL", Font.ITALIC, 20));
            g.drawString("Press S to save your score", WIDTH / 2 - 120, HEIGHT - 60);
        }

        // show timer for powerUp
        if (head.isPoweredUp()) {
            int timer = 10 - head.getNumTicks() / 50;

            switch (head.getPowerType()) {
                case SpeedPower:
                    g.setColor(Color.RED);
                    break;
                case InvulnerablePower:
                    g.setColor(Color.GREEN);
                    break;
                default:
                    System.out.println("Error: invalid powerType");
            }
            g.setFont(new Font("ARIEL", 0, 25));
            g.drawString("" + timer, WIDTH / 2 - 20, 25);
        }

        handler.render(g);

        g.dispose();
        bs.show();
    }

    public static int clamp(int var, int min, int max) {
        if (var >= max) {
            return var = max;
        } else if (var <= min) {
            return var = min;
        } else {
            return var;
        }
    }

    private void createPowerUp() {
        if (head.getVelX() == 0 && head.getVelY() == 0) {
            numTicks = 0;
        } else {
            if (numTicks == 1000) {
                PowerUp powerUp;
                int randNum = r.nextInt(2);
                switch (randNum) {
                    case 0:
                        powerUp = new SpeedPower(r.nextInt(WIDTH - 22), r.nextInt(HEIGHT - 54), handler);
                        break;
                    case 1:
                        powerUp = new InvulnerablePower(r.nextInt(WIDTH - 22), r.nextInt(HEIGHT - 54), handler);
                        break;
                    default:
                        System.out.println("Error: invalid random number (" + randNum + ")");
                        powerUp = new SpeedPower(r.nextInt(WIDTH - 22), r.nextInt(HEIGHT - 54), handler);
                }
                handler.addObject(powerUp);
                numTicks = 0;
            }
            numTicks++;
        }
    }

    public static void main(String args[]) {
        new Game();
    }
}
