package hyperchessab.hyperchess;

/**
 * Created by Perlwin on 11/01/2015.
 */
public class GameListing {

    String name;
    String id;
    String password;
    int players;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }




}
