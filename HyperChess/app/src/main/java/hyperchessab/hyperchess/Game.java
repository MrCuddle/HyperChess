package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class Game {

    public enum GameState { Moving, Attacking }
    GameState currentGameState = GameState.Moving;

    ArrayList<Player> players = new ArrayList<Player>();
    int currentPlayer;

    GameBoard board;
    Camera camera;

    public Game(Context context, Camera camera){
        board = new GameBoard(context);
        this.camera = camera;

        players.add(new Player());
        players.add(new Player());
        currentPlayer = 0;
    }

    public void Update(double dt){
        board.Update(dt);

        switch(currentGameState){
            case Moving:
                break;
            case Attacking:
                break;
        }

    }

    public void Draw(Canvas c){
        c.setMatrix(camera.getTransform());
        c.drawColor(Color.BLACK);
        board.Draw(c);
    }

}
