package bombFever;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Elise Haram Vannes on 30.07.2017.
 */
public class StartScreenController implements Initializable {

    @FXML
    Button toScene2;

    public void showHighScores() throws IOException {
        Stage stage;
        Parent root;

        stage = (Stage) toScene2.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("HighScoreView.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene() throws IOException {
        Stage stage;
        Parent root;

        stage = (Stage) toScene2.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("GameScene.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void quit(){
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
