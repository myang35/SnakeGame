/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SnakeGame;

/**
 *
 * @author Marvin
 */
public class Score {
    
    private String name;
    private int points;

    public Score(String name, int points) {
        this.name = name;
        this.points = points;
    }
    
    public String toWriteFormat() {
        return name + "," + points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Score{" + "name=" + name + ", points=" + points + '}';
    }
}
