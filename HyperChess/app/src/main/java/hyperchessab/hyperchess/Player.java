package hyperchessab.hyperchess;

import java.util.ArrayList;

/**
 * Created by Perlwin on 30/12/2014.
 */
public class Player {
        private ArrayList<GamePiece> gamePieces;
        public int points;
        public String name;
        int teamColor, teamId;
        int primaryColor, secondaryColor, tertiaryColor;

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

    public void SetPrimaryColor(int color){
        primaryColor = color;
    }

    public void SetSecondaryColor(int color){
        secondaryColor = color;
    }

    public void SetTertiaryColor(int color){
        tertiaryColor = color;
    }

    public int GetPrimaryColor(){
        return primaryColor;
    }

    public int GetSecondaryColor(){
        return secondaryColor;
    }

    public int GetTertiaryColor(){
        return tertiaryColor;
    }

    public int GetTeamColor(){
        return teamColor;
    }

    public void SetPieces(ArrayList<GamePiece> pieces){
        this.gamePieces = pieces;
    }
}
