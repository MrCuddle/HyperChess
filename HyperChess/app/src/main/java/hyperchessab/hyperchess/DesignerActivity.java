package hyperchessab.hyperchess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

public class DesignerActivity extends ActionBarActivity implements ActionBar.TabListener, Piece1Fragment.Piece1Listener{

//    ViewPager viewPager;
//    Piece1FragmentAdapter adapter;
//    ArrayList<String> types = new ArrayList<>();
    ArrayList<GameManager.SavePiece> pieces;
    Piece1Fragment fragment;
    ActionBar actionBar;
    //Firebase fb;
    //ChildEventListener dbListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer);

        pieces = GameManager.GetUserSavePieces();

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            for (int i = 0; i < Settings.differentPieces; i++) {
                ActionBar.Tab tab = actionBar.newTab();
                tab.setText(pieces.get(i).name);
                tab.setTabListener(this);
                actionBar.addTab(tab);
            }

        }
        fragment = Piece1Fragment.newInstance(0, this);
        setFragment(fragment, false);

    }

    /*@Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        InitFirebase();
    }*/

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
    public void OnPieceNameChange(int index, String name) {
        if(actionBar != null){
            actionBar.getTabAt(index).setText(name);
        }
    }


//    public static class Piece1FragmentAdapter extends FragmentPagerAdapter{
//        ArrayList<String> types;
//        public Piece1FragmentAdapter(FragmentManager fm, ArrayList<String> types){
//            super(fm);
//            this.types = types;
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            return Piece1Fragment.newInstance(i, this);
//        }
//
//
//        @Override
//        public int getCount() {
//            return Settings.differentPieces;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return types.get(position);
//        }
//
//    }

    /*public void InitFirebase(){
        fb = new Firebase(DatabaseManager.URL).child("games").child(gameId);
        dbListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                boolean turnKey = dataSnapshot.getKey().equals("turn");
                boolean notMyTurn = !MyTurn();
                int playerNum = 0;
                if(turnKey) playerNum = (int)((long)dataSnapshot.child("player").getValue());
                //If it's not the local player's turn and a new turn is added to the database - execute the other player's move!
                if(turnKey && notMyTurn && playerNum != Game.this.localPlayerNumber){
                    int startX = (int)((long)dataSnapshot.child("moveStartX").getValue());
                    int startY = (int)((long)dataSnapshot.child("moveStartY").getValue());
                    int endX = (int)((long)dataSnapshot.child("moveEndX").getValue());
                    int endY = (int)((long)dataSnapshot.child("moveEndY").getValue());
                    int attackX = (int)((long)dataSnapshot.child("attackX").getValue());
                    int attackY = (int)((long)dataSnapshot.child("attackY").getValue());

                    ((GamePiece)board.GetTile(startX, startY).occupier).SimulateMove(endX, endY, attackX, attackY);

                    //Get rid of the turn from the database
                    fb.child("turn").removeValue();
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
    }*/

}
