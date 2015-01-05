package hyperchessab.hyperchess;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class DesignerActivity extends ActionBarActivity implements ActionBar.TabListener, Piece1Fragment.Piece1Listener{

//    ViewPager viewPager;
//    Piece1FragmentAdapter adapter;
//    ArrayList<String> types = new ArrayList<>();
    ArrayList<GameManager.SavePiece> pieces;
    Piece1Fragment fragment;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer);

//        for (int i = 0; i < Settings.differentPieces; i++) {
//            types.add("Name" + i);
//        }
//        adapter = new Piece1FragmentAdapter(getSupportFragmentManager(), types);
//        viewPager = (ViewPager)findViewById(R.id.designer_pager);
//        viewPager.setAdapter(adapter);

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

}
