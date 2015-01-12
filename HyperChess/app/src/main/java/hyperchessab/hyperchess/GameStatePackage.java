package hyperchessab.hyperchess;

import java.util.ArrayList;

/**
 * Created by Perlwin on 12/01/2015.
 */
public class GameStatePackage {

    public boolean online;
    public int playerNumber;
    public int currentPlayer;
    public int player1Points;
    public int player2Points;
    public Game.GameState currentGameState;

    public ArrayList<PieceState> pieces;

}
