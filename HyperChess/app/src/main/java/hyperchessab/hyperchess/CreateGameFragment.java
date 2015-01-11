package hyperchessab.hyperchess;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGameFragment extends Fragment {

    CreateGameFragmentListener listener;

    CheckBox chkPassword;
    EditText txtName, txtPassword;
    Button btnCreate;

    public CreateGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_game, container, false);
        chkPassword = (CheckBox)v.findViewById(R.id.chk_password);
        txtName = (EditText)v.findViewById(R.id.game_name);
        txtPassword = (EditText)v.findViewById(R.id.game_password);
        btnCreate = (Button)v.findViewById(R.id.btn_create);

        chkPassword.setChecked(false);
        txtPassword.setEnabled(false);


        chkPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkPassword.isChecked()){
                    txtPassword.setEnabled(true);
                } else {
                    txtPassword.setEnabled(false);
                }
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Publish to the listings and switch fragment...

                Firebase fb = new Firebase(DatabaseManager.URL).child("games").push();
                final GameListing g = new GameListing();
                g.setName(txtName.getText().toString());
                g.setId(fb.getKey());
                if(chkPassword.isChecked()){
                    g.setPassword(txtPassword.getText().toString());
                } else {
                    g.setPassword("");
                }
                fb.setValue(g, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        listener.onCreateGame(g);
                    }
                });


            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (CreateGameFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CreateGameFragmentListener");
        }
    }


    public interface CreateGameFragmentListener {
        public void onCreateGame(GameListing g);
    }

}
