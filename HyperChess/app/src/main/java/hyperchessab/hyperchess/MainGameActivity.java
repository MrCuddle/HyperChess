package hyperchessab.hyperchess;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class MainGameActivity extends ActionBarActivity implements MainMenuFragment.OnMainMenuInteractionListener, OptionFragment.OnOptionInteractionListener, LobbyFragment.LobbyFragmentListener, CreateGameFragment.CreateGameFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
//        Debug.startMethodTracing("trace");
        //getFragmentManager().beginTransaction().replace(R.id.fragment_container,new GameFragment(),"game_fragment").commit();
        setFragment(new MainMenuFragment(), false);
        GameManager.Load(this);

        Firebase.setAndroidContext(this);
        FirebaseLogin();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_game, menu);
        return true;
    }

    private void setFragment(Fragment f, boolean addToBackStack){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(addToBackStack){
            ft.replace(R.id.fragment_container,f, f.getTag()).addToBackStack(null);
        } else {
            ft.replace(R.id.fragment_container, f, f.getTag());
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() <= 0){
            super.onBackPressed();
        }
        else{
           getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onExitPressed() {
        Toast.makeText(this, "Exit Pressed", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onOptionsPressed() {
        setFragment(new OptionFragment(), true);
        Toast.makeText(this, "Options Pressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayPressed() {
        setFragment(GameFragment.newInstance(false,0,""), true);
    }

    @Override
    public void onDesignerPressed() {
        Toast.makeText(this, "Designer Pressed", Toast.LENGTH_SHORT).show();
        Intent getChatScreen = new Intent(this, DesignerActivity.class);

        startActivity(getChatScreen);
        //finish();

    }

    public void onCreatePressed(){
        setFragment(new CreateGameFragment(), true);
        Toast.makeText(this, "Create Game Pressed", Toast.LENGTH_SHORT).show();

    }

    public void onJoinPressed(){
        setFragment(new LobbyFragment(), true);
        Toast.makeText(this, "Join Game Pressed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onOptionInteraction(Uri uri) {
        //placeholder
        int i = 3 +4;
    }

    @Override
    public void onGameClicked(GameListing g) {
        //Start game as player 2
        setFragment(GameFragment.newInstance(true,1, g.getId()), true);
    }

    @Override
    public void onCreateGame(GameListing g) {
        //Start game as player 1
        setFragment(GameFragment.newInstance(true,0, g.getId()), true);
    }

    private void FirebaseLogin(){
        Firebase firebase = new Firebase(DatabaseManager.URL);
        firebase.authAnonymously(new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {

                Toast.makeText(MainGameActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

                switch(firebaseError.getCode()) {
                    default:
                        Toast.makeText(MainGameActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
