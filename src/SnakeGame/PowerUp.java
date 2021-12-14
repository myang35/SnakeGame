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
public abstract class PowerUp extends GameObject {
    protected Handler handler;
    protected Color color;
    protected Random r = new Random();
    
    private int size;

    public PowerUp(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        
        this.handler = handler;
        this.size = 16;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public void tick() {
        collision();
    }
    
    private void collision() {
        for (int i = 0; i < handler.object.size(); i++) {
            
            GameObject tempObject = handler.object.get(i);
            
            if (tempObject.getId() == ID.Body) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    x = r.nextInt(Game.WIDTH - this.size - 16);
                    y = r.nextInt(Game.HEIGHT - this.size - 39);
                }
            }
            if (tempObject.getId() == ID.Head) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    handler.removeObject(this);
                }
            }
            
        }
    }
    
    public void render(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, size, size);
    }
}
