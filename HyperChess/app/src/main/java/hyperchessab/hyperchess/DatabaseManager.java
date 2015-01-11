package hyperchessab.hyperchess;

/**
 * Created by Perlwin on 11/01/2015.
 */
public class DatabaseManager {

    static final String URL = "https://glaring-inferno-2930.firebaseio.com/";
    //static final String URL = "https://glaring-inferno-2930.firebaseio.com/";


    private DatabaseManager(){}
    static DatabaseManager instance = null;

    public static DatabaseManager getInstance(){
        if(instance == null)
            instance = new DatabaseManager();
        return instance;
    }
}
