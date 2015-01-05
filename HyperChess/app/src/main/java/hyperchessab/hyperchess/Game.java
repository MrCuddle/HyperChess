package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

import java.util.ArrayList;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class Game {

    public enum GameState { Moving, Attacking }
    public GameState currentGameState = GameState.Moving;

    //Flyttas till PlayerManager senare
    public ArrayList<Player> players = new ArrayList<Player>();
    int currentPlayer;

    GameBoard board;
    Camera camera;
    HUD hud;

    public Game(Context context, Camera camera){
        players.add(new Player(0));
        players.add(new Player(1));
        players.get(0).SetTeamColor(Color.RED);
        players.get(1).SetTeamColor(Color.BLUE);
        board = new GameBoard(context,this);
        this.camera = camera;
        camera.setBounds(board.Width * GameBoard.TileSize, board.Height * GameBoard.TileSize);
        currentPlayer = 0;
        hud = new HUD();
    }

    public void Update(double dt){
        board.Update(dt);

        switch(currentGameState){
            case Moving:
                break;
            case Attacking:
                break;
        }

        hud.Update();
    }

    public void IncrementCurrentPlayer(){
        currentPlayer = (currentPlayer + 1) % 2;
        hud.SetCurrentPlayer(currentPlayer);
    }

    public void Draw(Canvas c){
        c.setMatrix(camera.getTransform());
        c.drawColor(Color.BLACK);
        board.Draw(c);
        c.setMatrix(new Matrix());
        hud.Draw(c);
    }

}
