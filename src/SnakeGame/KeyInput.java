/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SnakeGame;

import static SnakeGame.Game.HEIGHT;
import static SnakeGame.Game.WIDTH;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 *
 * @author marvi
 */
public class KeyInput extends KeyAdapter {
    
    private Handler handler;
    private Random r = new Random();
    
    public KeyInput(Handler handler) {
        this.handler = handler;
        
        r = new Random();
    }
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int sleepTime = 70;
        
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            
            if (tempObject.getId() == ID.Head) {
                Head headObject = (Head)tempObject;
                int vel = headObject.getVelocity();
                // key events for player 1
                try {
                    if (key == KeyEvent.VK_UP && headObject.getHeading() != Heading.SOUTH) {
                        headObject.setHeading(Heading.NORTH);
                        headObject.setVelY(-vel);
                        headObject.setVelX(0);
                        Thread.sleep(sleepTime);
                    }
                    if (key == KeyEvent.VK_DOWN && headObject.getHeading() != Heading.NORTH) {
                        headObject.setHeading(Heading.SOUTH);
                        headObject.setVelY(vel);
                        headObject.setVelX(0);
                        Thread.sleep(sleepTime);
                    }
                    if (key == KeyEvent.VK_LEFT && headObject.getHeading() != Heading.EAST) {
                        headObject.setHeading(Heading.WEST);
                        headObject.setVelX(-vel);
                        headObject.setVelY(0);
                        Thread.sleep(sleepTime);
                    }
                    if (key == KeyEvent.VK_RIGHT && headObject.getHeading() != Heading.WEST) {
                        headObject.setHeading(Heading.EAST);
                        headObject.setVelX(vel);
                        headObject.setVelY(0);
                        Thread.sleep(sleepTime);
                    }
                } catch (InterruptedException ie) {
                    System.out.println(ie);
                }
            }
        }
        
        if (key == KeyEvent.VK_ENTER) restart();
        if (key == KeyEvent.VK_ESCAPE) System.exit(1);
        if (key == KeyEvent.VK_P && !Game.gameOver) pause();
        if (key == KeyEvent.VK_S && Game.gameOver) Game.scoresHandler.addScore(Game.points);
        
    }
    
    private void restart() {
        handler.clearObject();
        
        Game.gameOver = false;
        
        Game.points = 0;
        
        Game.head = new Head(Game.WIDTH/2, Game.HEIGHT/2, handler);
        handler.addObject(Game.head);

        Game.food = new Food(r.nextInt(WIDTH-22), r.nextInt(HEIGHT-54), handler);
        handler.addObject(Game.food);
    }
    
    private void pause() {
        if (Game.paused) {
            Game.paused = false;
        } else {
            Game.paused = true;
        }
    }
}
