package bombFever;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The class that controls what happens in the game.
 * Created by Elise Haram Vannes on 30.07.2017.
 */
public class GameBoard {

    private GraphicsContext gc;
    private double canvasWidth;
    private double canvasHeight;
    private int boardWidth = 16;
    private int boardHeight = 16;
    private int blockSize = 50;
    private int[][] boardGrid = new int[boardWidth][boardHeight];
    private Player player1 = new Player(1,1);
    private Player player2 = new Player(boardWidth-2,boardHeight-2);
    private Enemy enemy = new Enemy(boardWidth-2,1);
    private Image unbreakableBlock;
    private Image breakableBlock;
    private Image player1Img;
    private Image player2Img;
    private Image player1b;
    private Image enemyImg;
    private Image bomb;
    private Image explosion;
    private Image watermelonImg;
    private Image test;
    private Image expand;
    private Image expHorizontal;
    private Image expVertical;
    private Image expMiddle;
    private AnimationTimer anim;
    private long currentTime;
    private long deathTime;
    private List<Bomb> bombList = new ArrayList<>();
    private boolean gamePlaying = true;
    private boolean newLevel = false;
    private Label player1Score;
    private Label player2Score;
    private Label player1Life;
    private Label player2Life;
    private String levelColor = "#A1D490";

    // numbers representing game objects:
    // 0: empty (background)
    // 1: unbreakable block
    // 2: breakable block
    // 3: player 1 --- bomb ticking
    // 4: eksplosjonens senter, 5: eksplosjon oppover eller nedover, 6: eksplosjon venstre eller høyre
    // 7: Collectable som gjør til at eksplosjonen øker rekkevidde med 2
    // 8: Collectable som gjør til at man kan droppe meir enn 1 bombe av gangen, øker med 1 eller 2

    /**
     * This class represents the gameBoard, and takes care of actions between the players and the board.
     * @param gc            the graphicsContext for the canvas
     * @param canvas        the canvas that is drawn upon
     * @param player1Score  label that shows player 1's score
     * @param player2Score  label that shows player 2's score
     * @param player1Life   label that shows player 1's life
     * @param player2Life   label that shows player 2's life
     */
    public GameBoard(GraphicsContext gc, Canvas canvas, Label player1Score, Label player2Score, Label player1Life, Label player2Life){
        this.gc = gc;
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.player1Life = player1Life;
        this.player2Life = player2Life;
        enemy.setStartTime(System.nanoTime()/3);
        initImages();
        initBoard();
        drawBoard();
        player1.setPlayerGrid(boardGrid);
        player2.setPlayerGrid(boardGrid);
        anim = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(newLevel && gamePlaying){
                    if(currentTime-deathTime > 900500000) {
                        //resetGame();
                        newLevel = false;
                    } else {
                        currentTime = System.nanoTime() / 3;
                    }
                } else if(!gamePlaying){
                    gameOver();
                    anim.stop();
                } else {
                    currentTime = System.nanoTime() / 3;
                    updateBombs(currentTime);
                    boolean movedEnemy = enemy.tryMoveEnemy(currentTime,boardGrid);
                    if(movedEnemy) enemy.setStartTime(currentTime);
                    drawBoard();
                }
            }
        };
    }

    /**
     * Starts the animation.
     */
    void play(){
        gamePlaying = true;
        anim.start();
    }

    /**
     * Draws the current gameBoard on the canvas, as well as players, bombs
     * and enemies that are separate from the board array.
     */
    private void drawBoard(){
        gc.setFill(Color.web(levelColor));
        gc.fillRect(0,0,canvasWidth,canvasHeight);

        int xAxis = 0;
        int yAxis = 0;

        for(int i = 0; i < boardGrid.length; i++){
            for(int j = 0; j < boardGrid[0].length; j++){
                if(boardGrid[i][j] == 1){
                    gc.drawImage(unbreakableBlock,xAxis,yAxis,blockSize,blockSize);
                } else if(boardGrid[i][j] == 2){
                    gc.drawImage(breakableBlock,xAxis,yAxis,blockSize,blockSize);
                } else if(boardGrid[i][j] == 3){
                    gc.drawImage(bomb,xAxis,yAxis,blockSize,blockSize);
                } else if(boardGrid[i][j] == 4){
                    gc.drawImage(expMiddle,xAxis,yAxis,blockSize,blockSize);
                } else if(boardGrid[i][j] == 5){
                    gc.drawImage(expVertical,xAxis,yAxis,blockSize,blockSize);
                } else if(boardGrid[i][j] == 6){
                    gc.drawImage(expHorizontal,xAxis,yAxis,blockSize,blockSize);
                } else if(boardGrid[i][j] == 7){
                    gc.drawImage(expand,xAxis,yAxis,blockSize,blockSize);
                }
                if(player1.checkPosition(i,j)){
                    gc.drawImage(player1b,xAxis,yAxis,blockSize,blockSize);
                }
                if(player2.checkPosition(i,j)){
                    gc.drawImage(player2Img,xAxis,yAxis,blockSize,blockSize);
                }
                if(enemy.checkPosition(i,j)){
                    gc.drawImage(watermelonImg,xAxis,yAxis,blockSize,blockSize);
                }
                xAxis += blockSize;
            }
            yAxis += blockSize;
            xAxis = 0;
        }
    }

    /**
     * Initializes the starting board. Places players, enemy and blocks on the board.
     * The breakable blocks are randomly placed around the board.
     */
    private void initBoard(){
        for(int i = 0; i < boardGrid.length; i++){
            for(int j = 0; j < boardGrid[0].length; j++){
                if(i == 0 || j == 0 || i == boardGrid.length-1 || j == boardGrid[0].length-1){
                    boardGrid[i][j] = 1;
                } else if(i == 1 && j == 1) {
                } else if(i == boardGrid.length-2 && j == boardGrid[0].length-2) {
                } else if(i == 1 && j == 2) {
                } else if(i == 2 && j == 1){
                } else if(i == boardGrid.length-2 && j == boardGrid[0].length-3){
                } else if(i == boardGrid.length-3 && j == boardGrid[0].length-2) {
                } else if(i == boardGrid.length-2 && j == 1){
                } else if(i % 3 == 0 && j % 3 == 0){
                    boardGrid[i][j] = 1;
                } else {
                    int block = (int)Math.floor(Math.random()*2);
                    if(block == 1) boardGrid[i][j] = 2;
                }
            }
        }
    }

    /**
     * Updates the player position according to key input and restraining factors
     * in the gameBoard, such as blocks.
     * @param player    the player to be moved
     * @param direction the direction the player attempts to move
     */
    public void updatePlayerPosition(int player,int direction){
        if(!newLevel && gamePlaying) {
            int[][] playerGrid;
            if (player == 1) {
                player1.movePlayer(direction, boardGrid);
                playerGrid = player1.getPlayerGrid();
            } else {
                player2.movePlayer(direction, boardGrid);
                playerGrid = player2.getPlayerGrid();
            }

            for (int i = 0; i < boardGrid.length; i++) {
                for (int j = 0; j < boardGrid[0].length; j++) {
                    if (playerGrid[i][j] == 0) {
                        boardGrid[i][j] = 0;
                    } else if (playerGrid[i][j] == 3) {
                        boardGrid[i][j] = 3;
                    }
                }
            }
            player1Score.setText(String.valueOf(player1.getScore()));
            player2Score.setText(String.valueOf(player2.getScore()));
        }
        //printBoard();
    }

    /**
     * Drops a bomb from a player.
     * @param player    the player that drops the bomb
     */
    public void dropBomb(int player){
        if(player == 1 && !player1.isBombDropped()){
            Bomb bomb = new Bomb(currentTime,player1);
            int x = player1.getPlayerXPosition();
            int y = player1.getPlayerYPosition();
            boardGrid[x][y] = 3;
            bomb.setBombPosition(x,y);
            bombList.add(bomb);
            player1.droppedBomb(true);
        }
        if(player == 2 && !player2.isBombDropped()){
            Bomb bomb = new Bomb(currentTime,player2);
            int x = player2.getPlayerXPosition();
            int y = player2.getPlayerYPosition();
            boardGrid[x][y] = 3;
            bomb.setBombPosition(x,y);
            bombList.add(bomb);
            player2.droppedBomb(true);
        }
    }

    /**
     * Updates all the current bombs. Checks the time to see when they should explode,
     * what to do when they have exploded, and when they are finished exploding.
     * When a bomb explodes it will try to remove nearby breakable blocks. It also
     * takes care of player interaction with the bombs.
     * @param currentTime   the current time in System.nanoTime()
     */
    private void updateBombs(long currentTime){
        if(bombList.size() != 0) {
            for (int i = 0; i < bombList.size(); i++) {
                Bomb currentBomb = bombList.get(i);
                Point pos = bombList.get(i).getBombPosition();
                // when the bomb has stopped ticking and is about to explode (before exploding)
                if (!currentBomb.isTicking(currentTime) && !currentBomb.getHasExploded()) {

                    int explosionLength = currentBomb.getOwner().getExplosionLength();

                    boolean explodeDownFinished = false;
                    boolean explodeRightFinished = false;
                    boolean explodeUpFinished = false;
                    boolean explodeLeftFinished = false;

                    for(int j = 1; j <= explosionLength; j++){

                        if(!explodeDownFinished){

                            if(isInBounds(boardGrid,pos.x+j,pos.y)){

                                if(boardGrid[pos.x+j][pos.y] == 0){
                                    boardGrid[pos.x+j][pos.y] = 5;
                                }
                                if (boardGrid[pos.x+j][pos.y] == 2) {
                                    currentBomb.getOwner().updateScore(10);
                                    boardGrid[pos.x+j][pos.y] = 5;
                                    explodeDownFinished = true;
                                }
                                if(boardGrid[pos.x+j][pos.y] == 1) explodeDownFinished = true;
                            }
                            if(j == explosionLength){
                                explodeDownFinished = true;
                            }
                            System.out.println("i loopen, j: " + j);
                            System.out.println("explodeDownFinished: " + explodeDownFinished);
                        }

                        if(!explodeRightFinished){

                            if(isInBounds(boardGrid,pos.x,pos.y+j)){
                                if(boardGrid[pos.x][pos.y+j] == 0){
                                    boardGrid[pos.x][pos.y+j] = 6;
                                }

                                if (boardGrid[pos.x][pos.y+j] == 2) {
                                    currentBomb.getOwner().updateScore(10);
                                    boardGrid[pos.x][pos.y+j] = 6;
                                    explodeRightFinished = true;
                                }
                                if(boardGrid[pos.x][pos.y+j] == 1) explodeRightFinished = true;
                            }
                            if(j == explosionLength){
                                explodeRightFinished = true;
                            }
                            System.out.println("i loopen, j: " + j);
                            System.out.println("explodeRightFinished: " + explodeRightFinished);
                        }
                    }

                    for(int j = 0; j >= -explosionLength; j--){

                        if(!explodeUpFinished){

                            if(isInBounds(boardGrid,pos.x+j,pos.y)){
                                if(boardGrid[pos.x+j][pos.y] == 0){
                                    boardGrid[pos.x+j][pos.y] = 5;
                                }

                                if (boardGrid[pos.x+j][pos.y] == 2) {
                                    currentBomb.getOwner().updateScore(10);
                                    boardGrid[pos.x+j][pos.y] = 5;
                                    explodeUpFinished = true;
                                }
                                if(boardGrid[pos.x+j][pos.y] == 1) explodeUpFinished = true;
                            }
                            if(j == -explosionLength){
                                explodeUpFinished = true;
                            }
                            System.out.println("i loopen, j: " + j);
                            System.out.println("explodeUpFinished: " + explodeUpFinished);
                        }

                        if(!explodeLeftFinished){

                            if(isInBounds(boardGrid,pos.x,pos.y+j)){
                                if(boardGrid[pos.x][pos.y+j] == 0){
                                    boardGrid[pos.x][pos.y+j] = 6;
                                }

                                if (boardGrid[pos.x][pos.y+j] == 2) {
                                    currentBomb.getOwner().updateScore(10);
                                    boardGrid[pos.x][pos.y+j] = 6;
                                    explodeLeftFinished = true;
                                }
                                if(boardGrid[pos.x][pos.y+j] == 1) explodeLeftFinished = true;
                            }
                            if(j == -explosionLength){
                                explodeLeftFinished = true;
                            }
                            System.out.println("i loopen, j: " + j);
                            System.out.println("explodeLeftFinished: " + explodeLeftFinished);
                        }
                    }
                    System.out.println("ferdig med begge for-loops");
                    if(boardGrid[pos.x][pos.y] != 1) boardGrid[pos.x][pos.y] = 4;

                    currentBomb.setHasExploded(true);
                    currentBomb.setStartTime(currentTime);
                    // when the bomb is about to explode (the clock ticks till it has finished)
                } else if(currentBomb.isTicking(currentTime) && currentBomb.getHasExploded()) {

                    if(intersectsExplodingBomb(player1,pos.x,pos.y,currentBomb.getOwner().getExplosionLength())){
                        Player currentOwner = currentBomb.getOwner();

                        if(currentOwner != player1) currentOwner.updateScore(1000);
                        gamePlaying = player1.decrementLife();
                        player1Life.setText(String.valueOf(player1.getLife()));
                        deathTime = System.nanoTime()/3;

                        if(gamePlaying)newLevel = true;
                    }
                    if(intersectsExplodingBomb(player2,pos.x,pos.y,currentBomb.getOwner().getExplosionLength())){
                        Player currentOwner = currentBomb.getOwner();

                        if(currentOwner != player2) currentOwner.updateScore(1000);
                        gamePlaying = player2.decrementLife();
                        player2Life.setText(String.valueOf(player2.getLife()));
                        deathTime = System.nanoTime()/3;

                        if(gamePlaying)newLevel = true;
                    }
                } else if(!currentBomb.isTicking(currentTime) && currentBomb.getHasExploded()) {
                    int explosionLength = currentBomb.getOwner().getExplosionLength();

                    for(int j = 1; j <= explosionLength; j++){

                        if(isInBounds(boardGrid,pos.x+j,pos.y) && boardGrid[pos.x+j][pos.y] == 5){

                            if((int)Math.floor(Math.random()*12) == 3){
                                boardGrid[pos.x+j][pos.y] = 7;
                            } else {
                                boardGrid[pos.x+j][pos.y] = 0;
                            }
                        }

                        if(isInBounds(boardGrid,pos.x,pos.y+j) && boardGrid[pos.x][pos.y+j] == 6){
                            if((int)Math.floor(Math.random()*12) == 3){
                                boardGrid[pos.x][pos.y+j] = 7;
                            } else {
                                boardGrid[pos.x][pos.y+j] = 0;
                            }
                        }
                    }

                    for(int j = 0; j >= -explosionLength; j--){

                        if(isInBounds(boardGrid,pos.x+j,pos.y) && boardGrid[pos.x+j][pos.y] == 5){
                            if((int)Math.floor(Math.random()*12) == 3){
                                boardGrid[pos.x+j][pos.y] = 7;
                            } else {
                                boardGrid[pos.x+j][pos.y] = 0;
                            }
                        }

                        if(isInBounds(boardGrid,pos.x,pos.y+j) && boardGrid[pos.x][pos.y+j] == 6){
                            if((int)Math.floor(Math.random()*12) == 3){
                                boardGrid[pos.x][pos.y+j] = 7;
                            } else {
                                boardGrid[pos.x][pos.y+j] = 0;
                            }
                        }
                    }

                    if(boardGrid[pos.x][pos.y] == 4) {
                        if((int)Math.floor(Math.random()*12) == 3){
                            boardGrid[pos.x][pos.y] = 7; // 7 = bombe-extension
                        } else {
                            boardGrid[pos.x][pos.y] = 0;
                        }
                    }
                    currentBomb.getOwner().droppedBomb(false);
                    bombList.remove(i);
                    // max = (a > b) ? a : b;
                }
                player1Score.setText(String.valueOf(player1.getScore()));
                player2Score.setText(String.valueOf(player2.getScore()));
            }
        }
    }

    /**
     * Checks if a player is intersecting with an exploding bomb.
     * @param player     the player to be checked
     * @param x          the x coordinate of the player
     * @param y          the y coordinate of the player
     * @param bombLength the length of the bomb
     * @return           true if the player has intersected with a bomb
     */
    private boolean intersectsExplodingBomb(Player player,int x, int y, int bombLength){
        int playerX = player.getPlayerXPosition();
        int playerY = player.getPlayerYPosition();
        boolean intersects = false;

        for(int i = -bombLength; i <= bombLength; i++){
            if (playerX == x+i && playerY == y && (boardGrid[x+i][y] == 5 || boardGrid[x+i][y] == 6)) intersects = true;
            if (playerX == x && playerY == y+i && (boardGrid[x][y+i] == 5 || boardGrid[x][y+i] == 6)) intersects = true;
        }
        return intersects;
    }

    /**
     * Performs a set of actions when the game is over.
     */
    private void gameOver(){
        saveTempScores();
        try {
            returnToMenu();
        }catch(IOException e){
            System.err.println("Cannot locate FXML-file.");
        }
    }

    /**
     * Saves the scores when a game is over in temporary storage, so they can be
     * accessed in the SaveScoreScreen.
     */
    private void saveTempScores(){
        // creates HighScore-object with the file to temporary save scores
        HighScore highScore = new HighScore("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\text\\tempHighScores.txt");
        // check if the file has content, if so, clear content
        highScore.eraseFile();
        // saves the two scores
        highScore.saveHighScore("player1",player1.getScore());
        highScore.saveHighScore("player2",player2.getScore());
    }

    /**
     * Returns the user to the main menu, is accessed through a button in the GUI.
     * @throws IOException
     */
    private void returnToMenu() throws IOException {
        Stage stage;
        Parent root;

        stage = (Stage) player1Score.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("SaveScoreScreen.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void resetGame(){
        player1.setPlayerPosition(1,1);
        player2.setPlayerPosition(boardWidth-2,boardHeight-2);
        player1.droppedBomb(false);
        player2.droppedBomb(false);
        bombList.clear();
        levelColor = getLevelColor();
        clearBoard();
        initBoard();
    }

    private void fillBoard(int fill){
        for(int i = 0; i < boardGrid.length; i++){
            for(int j = 0; j < boardGrid[0].length; j++){
                boardGrid[i][j] = fill;
            }
        }
    }

    /**
     * Removes everything from the board.
     */
    private void clearBoard(){
        for(int i = 0; i < boardGrid.length; i++){
            for(int j = 0; j < boardGrid[0].length; j++){
                boardGrid[i][j] = 0;
            }
        }
    }

    /**
     * Checks whether a position in an array is in bounds of the array.
     * @param array array to be checked
     * @param x     the x coordinate
     * @param y     the y coordinate
     * @return      true if the coordinates were in bounds
     */
    private boolean isInBounds(int[][] array, int x, int y){
        return !(x < 0 || y < 0 || x >= array.length || y >= array.length);
    }

    /**
     * Returns a randomly chosen color from a set of options.
     * @return the color that was chosen
     */
    private String getLevelColor(){
        int choice = (int)Math.floor(Math.random()*4);

        switch(choice){
            case 0:
                return "#DDF77C";
            case 1:
                return "#A1D490";
            case 2:
                return "#F77C9F";
            case 3:
                return "#A78FFF";
            default:
                return "#8FFFDF";
        }
    }

    /**
     * Attemps to initialize a number of pictures.
     */
    private void initImages(){
        unbreakableBlock = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\unbreakableBlock.png");
        breakableBlock = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\breakableBlock.png");
        player1Img = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\player1.png");
        player2Img = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\player2b.png");
        player1b = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\player1b.png");
        bomb = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\bomb.png");
        explosion = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\explosion.png");
        enemyImg = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\enemy.png");
        watermelonImg = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\watermelon.png");
        test = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\test.png");
        expand = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\expand.png");
        expHorizontal = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\expGeneral.png");
        expVertical = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\expVertical.png");
        expMiddle = FileHandling.initImage("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\img\\expMiddle.png");
    }
}
