package bombFever;

import java.awt.Point;

/**
 * Created by Elise Haram Vannes on 03.08.2017.
 */
public class Enemy {

    private Point enemyPosition = new Point(0,0);
    private int lives = 3;
    private int direction = 4; // 1 = up, 2 = right, 3 = down, 4 = left
    private long startTime;
    private boolean movable;

    public Enemy(int x, int y){
        enemyPosition.setLocation(x,y);
    }

    /**
     * Moves the enemy.
     * @param boardGrid the board the enemy is moving on
     */
    public void moveEnemy(int[][]boardGrid){
        int x = enemyPosition.x;
        int y = enemyPosition.y;

        movable = checkIfMovable(boardGrid,x,y);

        if((int)Math.floor(Math.random()*2) == 0){
            direction = getNewDirection(boardGrid, direction);
        }

        while(movable) {
            switch (direction) {
                case 1:
                    if (boardGrid[x - 1][y] == 0) {
                        enemyPosition.setLocation(x - 1, y);
                        movable = false;
                    }
                    else {
                        movable = checkIfMovable(boardGrid,x,y);
                        direction = (int) Math.ceil(Math.random() * 4);
                    }
                    break;
                case 2:
                    if (boardGrid[x][y + 1] == 0){
                        enemyPosition.setLocation(x, y + 1);
                        movable = false;
                    }
                    else {
                        movable = checkIfMovable(boardGrid,x,y);
                        direction = (int) Math.ceil(Math.random() * 4);
                    }
                    break;
                case 3:
                    if (boardGrid[x + 1][y] == 0){
                        enemyPosition.setLocation(x + 1, y);
                        movable = false;
                    }
                    else {
                        movable = checkIfMovable(boardGrid,x,y);
                        direction = (int) Math.ceil(Math.random() * 4);
                    }
                    break;
                case 4:
                    if (boardGrid[x][y - 1] == 0){
                        enemyPosition.setLocation(x, y - 1);
                        movable = false;
                    }
                    else {
                        movable = checkIfMovable(boardGrid,x,y);
                        direction = (int) Math.ceil(Math.random() * 4);
                    }
                    break;
            }
        }
    }

    /**
     * Tries to move the enemy if enough time has passed since it last moved.
     * @param currentTime   the current time
     * @param boardGrid     the board the enemy is moving on
     * @return              true if the player was moved
     */
    boolean tryMoveEnemy(long currentTime,int[][] boardGrid){
        if(currentTime-startTime > 400000000){
            moveEnemy(boardGrid);
            return true;
        }
        return false;
    }

    /**
     * Checks if it is possible to move the enemy.
     * @param boardGrid     the board the enemy is moving on
     * @param x             the x position
     * @param y             the y position
     * @return              true if the enemy can move
     */
    private boolean checkIfMovable(int[][] boardGrid,int x,int y){
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if(i == 0 && j == 0) continue;
                if(i == -1 && j == -1) continue;
                if(i == 1 && j == 1) continue;
                if(i == -1 && j == 1) continue;
                if(i == 1 && j == -1) continue;
                if(boardGrid[x+i][y+j] == 0) return true;
            }
        }
        return false;
    }

    /**
     * Returns a possible new direction for the enemy to move, first does a random check to see if it should.
     * @param boardGrid         the board the player is moving on
     * @param currentDirection  the direction the enemy currently is moving
     * @return                  either the currentDirection or a new direction
     */
    private int getNewDirection(int[][] boardGrid, int currentDirection){
        int x = enemyPosition.x;
        int y = enemyPosition.y;
        //boolean hasMoved = false;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) continue;

                if (boardGrid[x + i][y + j] == 0) {
                    int dir = (int) Math.floor(Math.random() * 2);
                    if (dir == 1){
                        int direction = determineDirection(x,y,x+i,y+j);
                        if(direction != 0) return direction;
                        return currentDirection;
                        //enemyPosition.setLocation(x+i,y+j); //return new Point(x + 1, y + j);
                        //hasMoved = true;
                    }
                }
            }
        }
        return currentDirection;
    }

    /**
     * Determines which direction the enemy should move.
     * @param x
     * @param y
     * @param newX
     * @param newY
     * @return
     */
    private int determineDirection(int x, int y, int newX, int newY){
        if(x < newX && y == newY) return 1;
        if(x > newX && y == newY) return 3;
        if(x == newX && y < newY) return 4;
        if(x == newX && y > newY) return 2;
        return 0;
    }

    void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public Point getEnemyPosition(){
        return enemyPosition;
    }

    boolean checkPosition(int x, int y){
        return x == enemyPosition.x && y == enemyPosition.y;
    }

    void setPosition(int x, int y){
        enemyPosition.setLocation(x,y);
    }

    void setMovable(boolean movable){
        this.movable = movable;
    }

    public int getLives(){
        return lives;
    }

    public void setLives(int lives){
        this.lives = lives;
    }
}
