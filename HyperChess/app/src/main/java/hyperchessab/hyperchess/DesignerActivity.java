package hyperchessab.hyperchess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Arrays;

public class DesignerActivity extends ActionBarActivity implements ActionBar.TabListener, Piece1Fragment.Piece1Listener{

//    ViewPager viewPager;
//    Piece1FragmentAdapter adapter;
//    ArrayList<String> types = new ArrayList<>();
    ArrayList<GameManager.SavePiece> pieces;
    Piece1Fragment fragment;
    ActionBar actionBar;
    Firebase fb;
    ChildEventListener dbListener;
    boolean online = false;
    int player = 0; //Only used if playing online
    String gameId; //Only used if playing online


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer);

        Intent intent = getIntent();
        if(intent != null){
            online = intent.getExtras().getBoolean("online", false);
            if(online){
                player = intent.getExtras().getInt("player", 0);
                gameId = intent.getExtras().getString("gameId","");
            }
        }
        pieces = GameManager.GetUserSavePieces();

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            for (int i = 0; i < Settings.differentPieces; i++) {
                ActionBar.Tab tab = actionBar.newTab();
                tab.setText("Piece " + Integer.toString(i + 1));
                tab.setTabListener(this);
                actionBar.addTab(tab);
            }
        }
        fragment = Piece1Fragment.newInstance(this);
        setFragment(fragment, false);

    }

    @Override
    protected void onPause() {
        super.onPause();

        //Remove firebase child event listener
        if(online){
            fb.removeEventListener(dbListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Set firebase child event listener
        if(online) {
            InitFirebase();
        }
    }

    private void setFragment(Fragment f, boolean addToBackStack){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(addToBackStack){
            ft.replace(R.id.designer_fragmentContainer,f, f.getTag()).addToBackStack(null);
        } else {
            ft.replace(R.id.designer_fragmentContainer, f, f.getTag());
        }
        ft.commit();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        if(fragment != null){
            fragment.ChangeCurrentPiece(tab.getPosition());

        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        //????
        int i  = 34;
        i++;
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        int i  = 34;
        i++;
    }

    @Override
    public void OnFinishedDesigning(PieceState[] result) {
        if(online) {
            ArrayList<PieceState> states = new ArrayList<PieceState>(Arrays.asList(result));
            if(player == 0) {
                GameManager.SetPlayer1Pieces(states);
                SendPieceDefinitionsToFirebase();
                if(GameManager.GetPlayer2Pieces() != null){
                    StartGame();
                }
            } else {
                GameManager.SetPlayer2Pieces(states);
                SendPieceDefinitionsToFirebase();
                if(GameManager.GetPlayer1Pieces() != null){
                    StartGame();
                }
            }
        } else {
            ArrayList<PieceState> states = new ArrayList<PieceState>(Arrays.asList(result));
            if(player == 0) {
                GameManager.SetPlayer1Pieces(states);
                player++;
            } else {
                GameManager.SetPlayer2Pieces(states);
                StartGame();
            }
        }
    }

    @Override
    public void RequestTabChange(int tab) {
        getSupportActionBar().setSelectedNavigationItem(tab);
    }


    public void OnPieceNameChange(int index, String name) {
        if(actionBar != null){
            actionBar.getTabAt(index).setText(name);
        }
    }

    public void SendPieceDefinitionsToFirebase(){

        ArrayList<PieceState> pieceStates;

        Firebase ref;
        if(player == 0) {
            pieceStates = GameManager.GetPlayer1Pieces();
        } else {
            pieceStates = GameManager.GetPlayer2Pieces();
        }
        for (int i = 0; i < pieceStates.size(); i++) {
            if(player == 0) {
                ref = fb.child("player1").push();
            } else {
                ref = fb.child("player2").push();
            }
            ref.child("piece").setValue(pieceStates.get(i));
//            ref.child("HP").setValue(pieceStates.get(i).HP);
//            ref.child("attackRange").setValue(pieceStates.get(i).attackRange);
//            ref.child("shapeType").setValue(pieceStates.get(i).shapeType);
//            ref.child("movePatterns").setValue(pieceStates.get(i).movePatterns);
        }

    }

    void StartGame(){

        Intent intent = new Intent(this, MainGameActivity.class);
        intent.putExtra("startgame", online);
        intent.putExtra("online", online);
        intent.putExtra("player", player);
        intent.putExtra("gameId", gameId);

        startActivity(intent);

    }

    public void InitFirebase(){
        fb = new Firebase(DatabaseManager.URL).child("games").child(gameId);
        dbListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //We're interested in pieces sent by the other player
                boolean otherPlayer;
                if(player == 0)
                    otherPlayer = dataSnapshot.getKey().equals("player2");
                else
                    otherPlayer = dataSnapshot.getKey().equals("player1");

                //If the pieces were designed by the other player....
                if(otherPlayer){
                    if(player == 0){
                        ArrayList<PieceState> pieceStates = new ArrayList<PieceState>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){

                            PieceState ps = new PieceState();
                            ps.HP = (int)((long)ds.child("piece").child("HP").getValue());
                            ps.attackRange = (int)((long)ds.child("piece").child("attackRange").getValue());
                            ps.shapeType = (int)((long)ds.child("piece").child("shapeType").getValue());
                            ps.movePatterns = (ArrayList<MovePattern>)ds.child("piece").child("movePatterns").getValue();
                            pieceStates.add(ps);
                        }
                        GameManager.SetPlayer2Pieces(pieceStates);
                        if(GameManager.GetPlayer1Pieces() != null){
                            StartGame();
                        }
                    } else {
                        ArrayList<PieceState> pieceStates = new ArrayList<PieceState>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){

                            PieceState ps = (PieceState)ds.child("piece").getValue();
//                            ps.HP = (int)((long)ds.child("HP").getValue());
//                            ps.attackRange = (int)((long)ds.child("attackRange").getValue());
//                            ps.shapeType = (int)((long)ds.child("shapeType").getValue());
//                            ps.movePatterns = (ArrayList<MovePattern>)ds.child("movePatterns").getValue();
                            pieceStates.add(ps);
                        }
                        GameManager.SetPlayer1Pieces(pieceStates);
                        if(GameManager.GetPlayer2Pieces() != null){
                            StartGame();
                        }
                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        fb.addChildEventListener(dbListener);
    }

}
