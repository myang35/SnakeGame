package SnakeGame.PowerUps;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import SnakeGame.Handler;
import SnakeGame.Handler;
import SnakeGame.ID;
import SnakeGame.ID;
import SnakeGame.PowerUp;
import SnakeGame.PowerUp;
import java.awt.Color;

/**
 *
 * @author marvi
 */
public class SpeedPower extends PowerUp {
    
    public SpeedPower(int x, int y, Handler handler) {
        super(x, y, ID.SpeedPower, handler);
        
        color = Color.RED;
    }
    
}
