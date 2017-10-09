package bombFever;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;

/**
 * Created by Elise Haram Vannes on 25.08.2017.
 */
public class OnePlayerBoard extends GameBoard {

    public OnePlayerBoard(GraphicsContext gc, Canvas canvas, Label player1Score, Label player2Score, Label player1Life, Label player2Life) {
        super(gc, canvas, player1Score, player2Score, player1Life, player2Life);
    }
}
