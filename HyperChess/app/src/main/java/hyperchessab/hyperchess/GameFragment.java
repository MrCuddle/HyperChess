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
import android.view.View;
import android.view.ViewGroup;


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
        gameView = new GameView(getActivity(), online, player, id);
        setHasOptionsMenu(true);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //Load saved game state if one exists, otherwise, populate the game with the default starting layout
        if(sharedPref.getBoolean("ingame",false)){
            gameView.game.LoadGameStateFromJSON(sharedPref.getString("gamestate",""));
        } else {
            gameView.game.board.AddObjects();
        }

        return gameView;
    }

    @Override
    public void onDestroyView() {

        gameView.game.RemoveListeners();
        Log.d("GAME", "DESTROYED");


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String state = gameView.game.GameStateToJSON();

        editor.putBoolean("online", online);
        editor.putBoolean("ingame", true);
        editor.putString("gamestate",state);
        editor.commit();


        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_in_game, menu);
        super.onCreateOptionsMenu(menu, inflater);
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