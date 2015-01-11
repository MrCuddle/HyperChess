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
        players.get(0).SetPrimaryColor(Color.argb(255,147,79,236));
        players.get(0).SetSecondaryColor(Color.argb(255,156,93,238));
        players.get(0).SetTertiaryColor(Color.argb(255,179,131,242));
        players.get(1).SetPrimaryColor(Color.argb(255,228,21,21));
        players.get(1).SetSecondaryColor(Color.argb(255,234,39,39));
        players.get(1).SetTertiaryColor(Color.argb(255,239,90,90));
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
        //c.drawColor(Color.BLACK);
        board.Draw(c);
        c.setMatrix(new Matrix());
        hud.Draw(c);
    }

}
