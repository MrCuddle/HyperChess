package hyperchessab.hyperchess;

import java.util.ArrayList;

/**
 * Created by Perlwin on 30/12/2014.
 */
public class Player {
        private ArrayList<GamePiece> gamePieces;
        public int points;
        public String name;

        public Player(int points, String name){
            this.points = points;
            this.name = name;
        }

        public Player(){
            this.points = Settings.playerPoints;
            this.name = Settings.defaultPlayerName;
        }

}
