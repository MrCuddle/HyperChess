package hyperchessab.hyperchess;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.firebase.client.Firebase;


public class MainGameActivity extends ActionBarActivity implements MainMenuFragment.OnMainMenuInteractionListener, OptionFragment.OnOptionInteractionListener, LobbyFragment.LobbyFragmentListener, CreateGameFragment.CreateGameFragmentListener {
    boolean gameStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

//        Debug.startMethodTracing("trace");
        //getFragmentManager().beginTransaction().replace(R.id.fragment_container,new GameFragment(),"game_fragment").commit();

        GameManager.Load(this);

        Firebase.setAndroidContext(this);


//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putBoolean("ingame", false);
//        editor.commit();

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            if(intent.getExtras().getBoolean("startgame", false)){
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("ineditor", false);
//                editor.putBoolean("ingame", true);
                editor.putBoolean("online", intent.getExtras().getBoolean("online", false));
                editor.putString("gameId", intent.getExtras().getString("gameId", ""));
                editor.putInt("player", intent.getExtras().getInt("player", 0));
                editor.commit();
                //setFragment(new MainMenuFragment(), false);
                setFragment(GameFragment.newInstance(intent.getExtras().getBoolean("online", false),
                                                        intent.getExtras().getInt("player", 0),
                                                        intent.getExtras().getString("gameId", "")), true);
                gameStarted = true;
            } else {
                setFragment(new MainMenuFragment(), false);
            }
        } else {
            setFragment(new MainMenuFragment(), false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.action_bar_game);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.hide();
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

    private void StartDesigner(boolean online, int player, String gameId){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("ineditor", true);
        editor.putBoolean("online", online);
        editor.putString("gameId",gameId);
        editor.putInt("player", player);
        editor.commit();


        Intent designerIntent = new Intent(this, DesignerActivity.class);
        designerIntent.setFlags(designerIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        designerIntent.putExtra("online", online);
        designerIntent.putExtra("player", player);
        designerIntent.putExtra("gameId", gameId);

        startActivity(designerIntent);
    }

    @Override
    public void onBackPressed() {
        if(gameStarted){
            gameStarted = false;
            super.onBackPressed();
        }
        if(getFragmentManager().getBackStackEntryCount() <= 0){
            super.onBackPressed();
        }
        else{
           getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onPlayPressed() {
        StartDesigner(false, 0, "");
    }

    public void onCreatePressed(){
        setFragment(new CreateGameFragment(), true);
        //Toast.makeText(this, "Create Game Pressed", Toast.LENGTH_SHORT).show();

    }

    public void onJoinPressed(){
        setFragment(new LobbyFragment(), true);
        //Toast.makeText(this, "Join Game Pressed", Toast.LENGTH_SHORT).show();

    }

    public void onContinuePressed(){
        //Correct parameters not needed since they'll be filled in later
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        if(sharedPref.getBoolean("ineditor",false)){
            StartDesigner(sharedPref.getBoolean("online",false),sharedPref.getInt("player",0),sharedPref.getString("gameId",""));
        } else {
            setFragment(GameFragment.newInstance(false, 0, ""), true);
        }
    }

    public void onForfeitPressed(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("ineditor", false);
        editor.putBoolean("ingame", false);
        editor.commit();

        if(sharedPref.getBoolean("online", false)) {
            Firebase fb = new Firebase(DatabaseManager.URL).child("games").child(sharedPref.getString("gameId", ""));
            fb.child("forfeit").setValue(0);
            fb.child("players").setValue(2);
        }

        setFragment(new MainMenuFragment(), false);
    }

    @Override
    public void onOptionInteraction(Uri uri) {
        //placeholder
        int i = 3 +4;
    }

    @Override
    public void onGameClicked(GameListing g) {
        //Start game as player 2
        //setFragment(GameFragment.newInstance(true,1, g.getId()), true);
        StartDesigner(true,1,g.getId());
    }

    @Override
    public void onCreateGame(GameListing g) {
        //Start game as player 1
        //setFragment(GameFragment.newInstance(true,0, g.getId()), true);

        StartDesigner(true, 0, g.getId());
    }

    public void GameOver(){
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        setFragment(new MainMenuFragment(), false);

    }

}
