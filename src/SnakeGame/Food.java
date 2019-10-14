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
    private Random r = new Random();

    public Food(int x, int y, Handler handler) {
        super(x, y, ID.Food);
        
        this.handler = handler;
        
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, 16, 16);
    }

    public void tick() {
        collision();
    }
    
    private void collision() {
        for (int i = 0; i < handler.object.size(); i++) {
            
            GameObject tempObject = handler.object.get(i);
            
            if (tempObject.getId() == ID.Head || tempObject.getId() == ID.Body) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    x = r.nextInt(Game.WIDTH-22);
                    y = r.nextInt(Game.HEIGHT-54);
                }
            }
            
        }
    }
    
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(x, y, 16, 16);
    }
    
}
