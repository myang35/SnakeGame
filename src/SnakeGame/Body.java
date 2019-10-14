/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SnakeGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author marvi
 */
public class Body extends GameObject {
    Random r = new Random();
    Handler handler;
    int bodyNum;
    
    public Body(int x, int y, int bodyNum, Handler handler) {
        super(x, y, ID.Body);
        this.handler = handler;
        this.bodyNum = bodyNum;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, 16, 16);
    }

    public void tick() {
        int numObjects = handler.object.size();
        
        // loop through all objects
        for (int i = 0; i < numObjects; i++) {
            
            GameObject tempObject = handler.object.get(i);
            
            // if there is a head object, place body in a previous head location
            if (tempObject.getId() == ID.Head) {
                Head headObject = (Head)tempObject;
                LinkedList<Integer> prevX = headObject.getPrevX();
                LinkedList<Integer> prevY = headObject.getPrevY();
                x = prevX.get(prevX.size() - 6 * bodyNum);
                y = prevY.get(prevY.size() - 6 * bodyNum);
            }
        }
            
        x = Game.clamp(x, 0, Game.WIDTH - 22);
        y = Game.clamp(y, 0, Game.HEIGHT - 54);
    }

    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, 16, 16);
    }
    
}
