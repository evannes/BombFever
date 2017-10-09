package bombFever;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Elise Haram Vannes on 20.08.2017.
 */
public class SaveScoreController implements Initializable{

    @FXML
    Button returnButton;

    @FXML
    TextField player1Name;

    @FXML
    TextField player2Name;

    @FXML
    Label player1ScoreField;

    @FXML
    Label player2ScoreField;

    private int player1Score = 0;
    private int player2Score = 0;
    private String player1NameString = "player 1";
    private String player2NameString = "player 2";

    public void saveScores() throws IOException{
        if(!player1Name.getText().equals("")){
            player1NameString = player1Name.getText();
        } else {
            player2NameString = "anonymous";
        }
        if(!player2Name.getText().equals("")) {
            player2NameString = player2Name.getText();
        } else {
            player2NameString = "anonymous";
        }
        HighScore highScore = new HighScore("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\text\\highScores.txt");
        highScore.saveHighScore(player1NameString,player1Score);
        highScore.saveHighScore(player2NameString,player2Score);
        changeScene("HighScoreView.fxml");
    }

    public void getScores(){
        HighScore highScore = new HighScore("C:\\Users\\Bruker\\IdeaProjects\\Bomberman\\src\\bombFever\\text\\tempHighScores.txt");
        List<List<String>> jsonOutput = highScore.showHighScores();
        if(jsonOutput.size() == 2 && jsonOutput.get(0).size() == 2){
            if(jsonOutput.get(1).get(0).equals("player1")) {
                player1Score = Integer.parseInt(jsonOutput.get(0).get(0));
                player2Score = Integer.parseInt(jsonOutput.get(0).get(1));
            } else if(jsonOutput.get(1).get(0).equals("player2")){
                player1Score = Integer.parseInt(jsonOutput.get(0).get(1));
                player2Score = Integer.parseInt(jsonOutput.get(0).get(0));
            }
        }
        for(int i = 0; i < jsonOutput.get(0).size(); i++){
            for(int j = 0; j < jsonOutput.size(); j++){
                System.out.println("jsonOutput.get(j).get(i) j= " + j + " i = " + i);
                System.out.println("Content:" + jsonOutput.get(j).get(i));
            }
        }
    }

    public void returnToMenu(ActionEvent event) throws IOException {
        changeScene("StartScreen.fxml");
    }

    public void changeScene(String fxmlFile) throws IOException {
        Stage stage;
        Parent root;

        stage = (Stage) returnButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(fxmlFile));

        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void getTempScores(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getScores();
        player1ScoreField.setText("Player 1 score: " + String.valueOf(player1Score));
        player2ScoreField.setText("Player 2 score: " + String.valueOf(player2Score));
    }
}
