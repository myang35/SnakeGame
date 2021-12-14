/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SnakeGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author marvi
 */
public class Food extends GameObject {
    
    private Handler handler;
    private Random r;
    
    private int size;

    public Food(int x, int y, Handler handler) {
        super(x, y, ID.Food);
        
        r = new Random();
        this.handler = handler;
        this.size = 16;
    }
    
    public Food(Handler handler) {
        this(0, 0, handler);
        
        this.x = r.nextInt(Game.WIDTH - this.size - 16);
        this.y = r.nextInt(Game.HEIGHT - this.size - 39);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, this.size, this.size);
    }

    public void tick() {
        collision();
    }
    
    private void collision() {
        for (int i = 0; i < handler.object.size(); i++) {
            
            GameObject tempObject = handler.object.get(i);
            
            if (tempObject.getId() == ID.Head || tempObject.getId() == ID.Body) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    x = r.nextInt(Game.WIDTH - this.size - 16);
                    y = r.nextInt(Game.HEIGHT - this.size - 39);
                }
            }
            
        }
    }
    
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(x, y, this.size, this.size);
    }
    
}
