package hyperchessab.hyperchess;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by Perlwin on 30/12/2014.
 */
public class Player {
        private ArrayList<GamePiece> gamePieces;
        public int points;
        public String name;
        int teamColor, teamId;

        public Player(int points, String name){
            this.points = points;
            this.name = name;
        }

    public Player(){
        this.points = Settings.playerPoints;
        this.name = Settings.defaultPlayerName;
    }

        public Player(int id){
            this.points = Settings.playerPoints;
            this.name = Settings.defaultPlayerName;
            teamId = id;
        }

        public int GetTeamId(){
            return teamId;
        }

        public void SetTeamColor(int color){
            teamColor = color;
        }

        public int GetTeamColor(){
            return teamColor;
        }
}
