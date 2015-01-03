package hyperchessab.hyperchess;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;


public class DesignerActivity extends FragmentActivity{

    ViewPager viewPager;
    Piece1FragmentAdapter adapter;
    ArrayList<String> types = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer);
//        setContentView(R.layout.activity_designer);
//        FragmentTransaction ft = fm.beginTransaction();
//        Piece1Fragment frag = Piece1Fragment.newInstance();
//        ft.replace(R.id.tabframe, frag, "piece1");
//        ft.addToBackStack("piece1");
//        ft.commit();
        for (int i = 0; i < Settings.differentPieces; i++) {
            types.add("Name" + i);
        }
        adapter = new Piece1FragmentAdapter(getSupportFragmentManager(), types);
        viewPager = (ViewPager)findViewById(R.id.designer_pager);
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_designer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class Piece1FragmentAdapter extends FragmentPagerAdapter{
        ArrayList<String> types;
        public Piece1FragmentAdapter(FragmentManager fm, ArrayList<String> types){
            super(fm);
            this.types = types;
        }

        @Override
        public Fragment getItem(int i) {
            return Piece1Fragment.newInstance();
        }


        @Override
        public int getCount() {
            return Settings.differentPieces;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return types.get(position);
        }

    }

}
