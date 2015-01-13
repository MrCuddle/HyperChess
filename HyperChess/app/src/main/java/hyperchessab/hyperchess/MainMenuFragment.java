package hyperchessab.hyperchess;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link hyperchessab.hyperchess.MainMenuFragment.OnMainMenuInteractionListener} interface
 * to handle interaction events.
 */
public class MainMenuFragment extends Fragment {

    Button play, options, exit, create, join, continue_game;

    private OnMainMenuInteractionListener mListener;

    private View.OnClickListener buttonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            onButtonPressed(v.getId());
        }
    };

    public MainMenuFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        play = (Button)v.findViewById(R.id.mainmenu_button_play);
        options = (Button)v.findViewById(R.id.mainmenu_button_options);
        exit = (Button)v.findViewById(R.id.mainmenu_button_exit);
        create = (Button)v.findViewById(R.id.mainmenu_button_create);
        join = (Button)v.findViewById(R.id.mainmenu_button_join);
        continue_game = (Button)v.findViewById(R.id.mainmenu_button_continue);

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("ingame",false)){
            create.setVisibility(View.GONE);
            join.setVisibility(View.GONE);
            play.setVisibility(View.GONE);
        } else {
            continue_game.setVisibility(View.GONE);
        }

        if(sharedPreferences.getBoolean("online", false)){
            continue_game.setEnabled(false);
        }
        //Disable the online play buttons until we connect to firebase
        create.setEnabled(false);
        join.setEnabled(false);

        play.setOnClickListener(buttonListener);
        options.setOnClickListener(buttonListener);
        exit.setOnClickListener(buttonListener);
        create.setOnClickListener(buttonListener);
        join.setOnClickListener(buttonListener);
        continue_game.setOnClickListener(buttonListener);

        Button b = (Button)v.findViewById(R.id.mainmenu_button_designer);
        b.setOnClickListener(buttonListener);

        FirebaseLogin();

        return v;
    }

    public void onButtonPressed(int buttonId) {
        if (mListener != null) {
            switch (buttonId){
                case R.id.mainmenu_button_play:
                    mListener.onPlayPressed();
                    break;
                case R.id.mainmenu_button_options:
                    mListener.onOptionsPressed();
                    break;
                case R.id.mainmenu_button_exit:
                    mListener.onExitPressed();
                    break;
                case R.id.mainmenu_button_designer:
                    mListener.onDesignerPressed();
                    break;
                case R.id.mainmenu_button_create:
                    mListener.onCreatePressed();
                    break;
                case R.id.mainmenu_button_join:
                    mListener.onJoinPressed();
                    break;
                case R.id.mainmenu_button_continue:
                    mListener.onContinuePressed();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMainMenuInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void FirebaseLogin(){
        Firebase firebase = new Firebase(DatabaseManager.URL);
        firebase.authAnonymously(new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {

                create.setEnabled(true);
                join.setEnabled(true);
                continue_game.setEnabled(true);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

                switch(firebaseError.getCode()) {
                    default:
                        Toast.makeText(getActivity(), "Could not connect to internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public interface OnMainMenuInteractionListener {
        public void onExitPressed();
        public void onOptionsPressed();
        public void onPlayPressed();
        public void onDesignerPressed();
        public void onCreatePressed();
        public void onJoinPressed();
        public void onContinuePressed();
    }

}
