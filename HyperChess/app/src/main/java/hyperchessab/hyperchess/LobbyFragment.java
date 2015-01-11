package hyperchessab.hyperchess;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LobbyFragment extends Fragment {

    LobbyFragmentListener listener;

    ChildEventListener dbListener;
    Firebase firebase;

    private ListView games;
    private ArrayList<GameListing> gamesList = new ArrayList<GameListing>();

    public LobbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lobby, container, false);
        games = (ListView)v.findViewById(R.id.list_view);

        gamesList.clear();

        LobbyAdapter adapter = new LobbyAdapter(getActivity(),gamesList);
        games.setAdapter(adapter);

        games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onGameClicked(gamesList.get(i));
            }
        });

        firebase = new Firebase(DatabaseManager.URL).child("games");
        dbListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                String name = (String)snapshot.child("name").getValue();
                String password = (String)snapshot.child("password").getValue();
                String id = (String)snapshot.child("id").getValue();
                GameListing g = new GameListing();
                g.setId(id);
                g.setPassword(password);
                g.setName(name);

                gamesList.add(g);
                ((LobbyAdapter)games.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot snapshot, String s) {
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        };
        firebase.addChildEventListener(dbListener);


        return v;
    }

    @Override
    public void onDestroyView() {

        firebase.removeEventListener(dbListener);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (LobbyFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LobbyFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public interface LobbyFragmentListener {
        public void onGameClicked(GameListing g);
    }
}
