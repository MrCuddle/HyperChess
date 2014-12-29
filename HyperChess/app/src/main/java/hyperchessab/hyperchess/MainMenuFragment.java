package hyperchessab.hyperchess;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link hyperchessab.hyperchess.MainMenuFragment.OnMainMenuInteractionListener} interface
 * to handle interaction events.
 */
public class MainMenuFragment extends Fragment {

    Button play, options, exit;

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

        play.setOnClickListener(buttonListener);
        options.setOnClickListener(buttonListener);
        exit.setOnClickListener(buttonListener);

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

    public interface OnMainMenuInteractionListener {
        public void onExitPressed();
        public void onOptionsPressed();
        public void onPlayPressed();
    }

}
