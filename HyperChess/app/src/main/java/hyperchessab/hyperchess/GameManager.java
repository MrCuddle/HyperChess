package hyperchessab.hyperchess;

/**
 * Created by Spellabbet on 2015-01-02.
 */
public class GameManager {

    static Player playerUser, playerOpponent;

    public static Player GetUser(){
        if(playerUser == null){
            playerUser = new Player();
        }
        return playerUser;
    }

    public static Player GetOpponent(){
        if(playerOpponent == null){
            playerOpponent = new Player();
        }
        return playerOpponent;
    }
    

}
