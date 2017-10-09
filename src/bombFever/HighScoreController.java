package bombFever;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Elise Haram Vannes on 20.08.2017.
 */
public class HighScoreController implements Initializable{

    @FXML
    Button returnButton;

    @FXML
    Canvas canvas;

    private GraphicsContext gc;

    public void returnToMenu() throws IOException {
        Stage stage;
        Parent root;

        stage = (Stage) returnButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Outputs the high scores on the current scene.
     */
    public void drawHighScores(){
        HighScore highScore = new HighScore("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\text\\highScores.txt");

        List<List<String>> jsonOutput = highScore.showHighScores();
        int x = 200;
        int y = 100;

        gc.setFill(Color.web("#a1ed6f"));
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        gc.setFont(new Font("AR Darling", 40));//34));
        gc.setFill(Color.web("#000000"));

        for(int i = 0; i < jsonOutput.get(0).size(); i++){
            if(i == jsonOutput.get(0).size()-1){
                gc.fillText(i+1 + ". ",135,y);
            } else {
                gc.fillText(i + 1 + ". ", 150, y);
            }
            for(int j = 0; j < jsonOutput.size(); j++){

                gc.fillText(jsonOutput.get(j).get(i),x,y);

                x += 130;
            }
            x = 200;
            y += 50;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();
        drawHighScores();
    }
}
