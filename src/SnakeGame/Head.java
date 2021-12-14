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
public class Head extends GameObject {
    
    private Random r = new Random();
    private Handler handler;
    private Heading heading;
    private int size;
    private int vel;
    private LinkedList<Integer> prevX = new LinkedList<>();
    private LinkedList<Integer> prevY = new LinkedList<>();
    private int bodyNum;
    private ID powerType;
    private boolean poweredUp;
    private int numTicks;
    private boolean invulnerable;
    private int ticksSinceLastTurn;
    private Heading pendingAction;
    
    public Head(int x, int y, Handler handler) {
        super(x, y, ID.Head);
        this.handler = handler;
        this.size = 16;
        this.vel = 4;
        this.bodyNum = 0;
        this.ticksSinceLastTurn = 0;
        this.pendingAction = null;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public void tick() {
        prevX.add(x);
        prevY.add(y);
        
        if (pendingAction != null && canTurn()) {
            goDirection(pendingAction);
        }
        
        x += velX;
        y += velY;
        
        x = Game.clamp(x, 0, Game.WIDTH - this.size - 16);
        y = Game.clamp(y, 0, Game.HEIGHT - this.size - 39);
        
        collision();
        powerEffect(powerType);
        
        this.ticksSinceLastTurn++;
    }
    
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, size, size);
    }
    
    public void goDirection(Heading heading) {
        switch (heading) {
            case NORTH:
                goUp();
                break;
            case SOUTH:
                goDown();
                break;
            case WEST:
                goLeft();
                break;
            case EAST:
                goRight();
                break;
        }
    }
    
    public void goUp() {
        if (heading == Heading.SOUTH) {
            return;
        }
        
        if (pendingAction == Heading.NORTH || (pendingAction == null && canTurn())) {
            heading = Heading.NORTH;
            velY = -vel;
            velX = 0;
            ticksSinceLastTurn = 0;
            pendingAction = null;
            System.out.println("pending NULL");
            return;
        }
        
        if (pendingAction == null && !canTurn() && heading != Heading.NORTH) {
            pendingAction = Heading.NORTH;
            System.out.println("pending North");
            return;
        }
    }
    
    public void goDown() {
        if (heading == Heading.NORTH) {
            return;
        }
        
        if (pendingAction == Heading.SOUTH || (pendingAction == null && canTurn())) {
            heading = Heading.SOUTH;
            velY = vel;
            velX = 0;
            ticksSinceLastTurn = 0;
            pendingAction = null;
            System.out.println("pending NULL");
            return;
        }
        
        if (pendingAction == null && !canTurn() && heading != Heading.SOUTH) {
            pendingAction = Heading.SOUTH;
            System.out.println("pending South");
            return;
        }
    }
    
    public void goLeft() {
        if (heading == Heading.EAST) {
            return;
        }
        
        if (pendingAction == Heading.WEST || (pendingAction == null && canTurn())) {
            heading = Heading.WEST;
            velX = -vel;
            velY = 0;
            ticksSinceLastTurn = 0;
            pendingAction = null;
            System.out.println("pending NULL");
            return;
        }
        
        if (pendingAction == null && !canTurn() && heading != Heading.WEST) {
            pendingAction = Heading.WEST;
            System.out.println("pending West");
            return;
        }
    }
    
    public void goRight() {
        if (heading == Heading.WEST) {
            return;
        }
        
        if (pendingAction == Heading.EAST || (pendingAction == null && canTurn())) {
            heading = Heading.EAST;
            velX = vel;
            velY = 0;
            ticksSinceLastTurn = 0;
            pendingAction = null;
            return;
        }
        
        if (pendingAction == null && !canTurn() && heading != Heading.EAST) {
            pendingAction = Heading.EAST;
        }
    }
    
    private void collision() {
        for (int i = 0; i < handler.object.size(); i++) {
            
            GameObject tempObject = handler.object.get(i);
            
            if (tempObject.getId() == ID.Food) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    Game.points++;
                    bodyNum++;
                    handler.addObject(new Body(prevX.get(prevX.size()-7), prevY.get(prevY.size()-7), bodyNum, handler));
                }
            }
            
            if (tempObject.getId() == ID.Body) {
                if (getBounds().intersects(tempObject.getBounds()) && !invulnerable) {
                    velX = 0;
                    velY = 0;
                    vel = 0;
                    Game.gameOver = true;
                }
            }
            
            if (tempObject.getId() == ID.SpeedPower) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    powerType = ID.SpeedPower;
                    poweredUp = true;
                    numTicks = 0;
                }
            }
            
            if (tempObject.getId() == ID.InvulnerablePower) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    powerType = ID.InvulnerablePower;
                    poweredUp = true;
                    numTicks = 0;
                }
            }
        }
    }
    
    private void powerEffect(ID power) {
        if (poweredUp && !Game.gameOver) {
            switch(power) {
                case SpeedPower:
                    // disable other power ups
                    disableInvulnerablePower();
                    
                    // implement power up
                    if (heading == Heading.NORTH) velY = -6;
                    if (heading == Heading.SOUTH) velY = 6;
                    if (heading == Heading.EAST) velX = 6;
                    if (heading == Heading.WEST) velX = -6;
                    vel = 6;
                    break;
                case InvulnerablePower:
                    // disable other power ups
                    disableSpeedPower();
                    
                    // implement power up
                    invulnerable = true;
                    break;
                default:
                    System.out.println("Error: invalid power ID");
            }
            
            // disable power up after 500 ticks (approximately 10 seconds)
            if (numTicks == 500) {
                disableSpeedPower();
                disableInvulnerablePower();
                poweredUp = false;
                numTicks = 0;
            }
            numTicks++;
            
        }
    }
    
    private void disableSpeedPower() {
        vel = 4;
        if (heading == Heading.NORTH) velY = -vel;
        if (heading == Heading.SOUTH) velY = vel;
        if (heading == Heading.EAST) velX = vel;
        if (heading == Heading.WEST) velX = -vel;
    }
    
    private void disableInvulnerablePower() {
        invulnerable = false;
    }
    
    private boolean canTurn() {
        return ticksSinceLastTurn * vel >= size;
    }
    
    public void setHeading(Heading heading) {
        this.heading = heading;
    }
    
    public Heading getHeading() {
        return heading;
    }
    
    public void setVelocity(int vel) {
        this.vel = vel;
    }
    
    public int getVelocity() {
        return vel;
    }

    public LinkedList<Integer> getPrevX() {
        return prevX;
    }

    public LinkedList<Integer> getPrevY() {
        return prevY;
    }

    public int getVel() {
        return vel;
    }

    public void setVel(int vel) {
        this.vel = vel;
    }

    public boolean isPoweredUp() {
        return poweredUp;
    }

    public void setPoweredUp(boolean poweredUp) {
        this.poweredUp = poweredUp;
    }

    public int getBodyNum() {
        return bodyNum;
    }

    public int getNumTicks() {
        return numTicks;
    }

    public ID getPowerType() {
        return powerType;
    }
    
    public int getTicksSinceLastTurn() {
        return ticksSinceLastTurn;
    }
    
}
