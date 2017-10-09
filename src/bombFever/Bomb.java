package bombFever;

import java.awt.*;

/**
 * Created by Elise Haram Vannes on 31.07.2017.
 */
public class Bomb {

    private Player owner;
    private long startTime;
    private Point bombPosition = new Point(0,0);
    private boolean hasExploded = false;
    private int explosionLength = 1;

    public Bomb(long startTime,Player owner){
        this.startTime = startTime;
        this.owner = owner;
    }

    /**
     * Checks if the bomb should wait in its current state.
     * @param currentTime   the current time
     * @return              true if it should remain in its current state
     */
    public boolean isTicking(long currentTime){
        if(currentTime-startTime > 300500000){
            return false;
        }
        return true;
    }

    void setExplosionLength(int length){
        this.explosionLength = length;
    }

    int getExplosionLength(){
        return explosionLength;
    }

    public void setBombPosition(int x, int y){
        bombPosition.x = x;
        bombPosition.y = y;
    }

    public Point getBombPosition(){
        return bombPosition;
    }

    boolean getHasExploded(){
        return hasExploded;
    }

    void setHasExploded(boolean hasExploded){
        this.hasExploded = hasExploded;
    }

    void setStartTime(long startTime){
        this.startTime = startTime;
    }

    Player getOwner(){
        return owner;
    }

    /*
    public void startTicking(){
        KeyValue kv = new KeyValue();
        Timeline timeline = new Timeline();
    }*/
}
