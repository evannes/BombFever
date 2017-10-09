package bombFever;

import java.awt.*;

/**
 * Created by Elise Haram Vannes on 30.07.2017.
 */
public class Player {

    private Point playerPosition = new Point(0,0);
    private int[][] playerGrid;
    private boolean droppedBomb = false;
    private int score = 0;
    private int life = 3;
    private int explosionLength = 1;

    public Player(int x,int y){
        setPlayerPosition(x,y);
    }

    /**
     * Moves the player on the gameBoard.
     * @param direction 1 = up, 2 = right, 3 = down, 4 = left
     * @param boardGrid the board the player is located on
     */
    void movePlayer(int direction,int[][] boardGrid){

        if(direction == 1){
            if(canMove(boardGrid,playerPosition.x-1,playerPosition.y)){
                setPlayerPosition(playerPosition.x-1, playerPosition.y);
                intersectsCollectable(boardGrid,playerPosition.x,playerPosition.y);
            }
        }
        else if(direction == 2){
            if(canMove(boardGrid,playerPosition.x,playerPosition.y+1)){
                setPlayerPosition(playerPosition.x, playerPosition.y+1);
                intersectsCollectable(boardGrid,playerPosition.x,playerPosition.y);
            }
        }
        else if(direction == 3){
            if(canMove(boardGrid,playerPosition.x+1,playerPosition.y)){
                setPlayerPosition(playerPosition.x+1, playerPosition.y);
                intersectsCollectable(boardGrid,playerPosition.x,playerPosition.y);
            }
        }
        else if(direction == 4){
            if(canMove(boardGrid,playerPosition.x,playerPosition.y-1)){
                setPlayerPosition(playerPosition.x, playerPosition.y-1);
                intersectsCollectable(boardGrid,playerPosition.x,playerPosition.y);
            }
        }
    }

    /**
     * Checks if a player intersects a collectable item.
     * @param boardGrid the array containing the gameboard
     * @param x the x value for the player being checked
     * @param y the y value for the player being checked
     */
    private void intersectsCollectable(int[][] boardGrid, int x, int y){

        if(boardGrid[x][y] == 7 || boardGrid[x][y] == 8){
            // Increases the explosionLength for the bombs the player can drop
            if(boardGrid[x][y] == 7){
                explosionLength++;
            }
            if(boardGrid[x][y] == 8){
                // add ability to add several bombs at the same time, +2 or +1
            }
            boardGrid[x][y] = 0;
            score+=100;
        }
    }

    /**
     * Checks if the player can move in the board.
     * @param boardGrid the array containing the gameboard
     * @param x the x value for the player being checked
     * @param y the y value for the player being checked
     * @return  true if the player can move
     */
    private boolean canMove(int[][] boardGrid, int x, int y){
        if(boardGrid[x][y] == 1 || boardGrid[x][y] == 2){
            return false;
        } else {
            return true;
        }
    }

    public void updateScore(int points){
        score += points;
    }

    public int getScore(){
        return score;
    }

    boolean decrementLife(){
        if(life>0){
            life--;
            return true;
        }
        return false;
    }

    boolean isAlive(){
        if(life > 0) return true;
        return false;
    }

    int getLife(){
        return life;
    }

    void droppedBomb(boolean droppedBomb){
        this.droppedBomb = droppedBomb;
    }

    boolean isBombDropped(){
        return droppedBomb;
    }

    void setExplosionLength(int length){
        this.explosionLength = length;
    }

    int getExplosionLength(){
        return explosionLength;
    }

    void setPlayerPosition(int x, int y){
        playerPosition.setLocation(x,y);
    }

    int getPlayerXPosition(){
        return playerPosition.x;
    }

    int getPlayerYPosition(){
        return playerPosition.y;
    }

    void setPlayerGrid(int[][] playerGrid){
        this.playerGrid = playerGrid;
    }

    boolean checkPosition(int x, int y){
        return x == playerPosition.x && y == playerPosition.y;
    }

    int[][] getPlayerGrid(){
        return playerGrid;
    }
}
