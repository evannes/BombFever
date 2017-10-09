package bombFever;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController implements Initializable{

    @FXML
    Button toScene1;
    @FXML
    Canvas canvas;
    @FXML
    Label player1Score;
    @FXML
    Label player2Score;
    @FXML
    Label player1Life;
    @FXML
    Label player2Life;

    private GraphicsContext gc;
    private GameBoard gb;

    public void takeKeyInput(KeyEvent event){
        if(event.getCode() == KeyCode.UP){
            gb.updatePlayerPosition(2,1);
        }
        if(event.getCode() == KeyCode.RIGHT){
            gb.updatePlayerPosition(2,2);
        }
        if(event.getCode() == KeyCode.DOWN){
            gb.updatePlayerPosition(2,3);
        }
        if(event.getCode() == KeyCode.LEFT){
            gb.updatePlayerPosition(2,4);
        }
        if(event.getCode() == KeyCode.W){
            gb.updatePlayerPosition(1,1);
        }
        if(event.getCode() == KeyCode.D){
            gb.updatePlayerPosition(1,2);
        }
        if(event.getCode() == KeyCode.S){
            gb.updatePlayerPosition(1,3);
        }
        if(event.getCode() == KeyCode.A){
            gb.updatePlayerPosition(1,4);
        }
        if(event.getCode() == KeyCode.CONTROL){
            gb.dropBomb(2);
        }
        if(event.getCode() == KeyCode.F){
            gb.dropBomb(1);
        }
        if(event.getCode() == KeyCode.SPACE){
            System.out.println("space is being pressed");
        }
    }

    public void changeScene() throws IOException{
        Stage stage;
        Parent root;

        stage = (Stage) toScene1.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void play(){
        gb.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();
        gb = new GameBoard(gc, canvas,player1Score,player2Score,player1Life,player2Life);
        play();
    }
}
