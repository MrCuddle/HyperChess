package hyperchessab.hyperchess;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {


    GameView gameView;

    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_game, container, false);
        gameView = new GameView(getActivity());
        return gameView;
    }

    @Override
    public void onPause() {
        gameView.StopGameLoop();
        super.onPause();

    }

    @Override
    public void onResume() {
        //gameView.StartGameLoop();
        super.onResume();

    }
}