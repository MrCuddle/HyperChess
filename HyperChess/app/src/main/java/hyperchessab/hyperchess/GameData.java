package hyperchessab.hyperchess;

/**
 * Created by Simon on 2015-01-05.
 */

enum GameState{
    none,
    moving,
    attacking
}
public class GameData {
    static int teamOneScore, teamTwoScore;
    public static GameState currentState;

    public GameData(){
        teamOneScore = 0;
        teamTwoScore = 0;
        currentState = GameState.none;
    }

    public static void IncrementScore(int teamId){
        if(teamId == 0)
            teamOneScore++;
        else
            teamTwoScore++;
    }

    public static int GetScore(int teamId)
    {
        if(teamId == 0)
            return teamOneScore;
        else
            return teamTwoScore;
    }
}
