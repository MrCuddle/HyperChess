package hyperchessab.hyperchess;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    int player;
    boolean online;
    String id;

    GameView gameView;

    public static GameFragment newInstance(boolean online, int player, String id){
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt("player", player);
        args.putBoolean("online", online);
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            player = getArguments().getInt("player");
            online = getArguments().getBoolean("online");
            id = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_game,null);


        gameView = new GameView(getActivity(), online, player, id);
        gameView.game.notificationText = (TextView)v.findViewById(R.id.notification_text);
        gameView.game.notificationPanel = (LinearLayout)v.findViewById(R.id.notification_panel);
        setHasOptionsMenu(true);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //Load saved game state if one exists, otherwise, populate the game with the default starting layout
        if(sharedPref.getBoolean("ingame",false)){
            gameView.game.LoadGameStateFromJSON(sharedPref.getString("gamestate",""));
        } else {
            gameView.game.board.AddObjects();
        }
        ((FrameLayout)v.findViewById(R.id.game_container)).addView(gameView);
        return v;
    }

    @Override
    public void onDestroyView() {

        gameView.game.RemoveListeners();
        Log.d("GAME", "DESTROYED");

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(gameView.game.currentGameState != Game.GameState.GameOver) {

            String state = gameView.game.GameStateToJSON();

            editor.putBoolean("online", online);
            editor.putBoolean("ingame", true);
            editor.putString("gamestate", state);

        } else {
            editor.putBoolean("ingame", false);
            editor.putBoolean("online", false);
        }

        editor.commit();


        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_in_game, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_forfeit:
                gameView.game.Forfeit();
                break;
        }
        return true;
    }

    @Override
    public void onPause() {
        gameView.StopGameLoop();
        super.onPause();
        ((ActionBarActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onResume() {
        gameView.StartGameLoop();
        super.onResume();
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.show();
    }
}