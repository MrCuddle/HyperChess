package hyperchessab.hyperchess;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainGameActivity extends ActionBarActivity implements MainMenuFragment.OnMainMenuInteractionListener, OptionFragment.OnOptionInteractionListener, Piece1Fragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        //getFragmentManager().beginTransaction().replace(R.id.fragment_container,new GameFragment(),"game_fragment").commit();
        setFragment(new MainMenuFragment(), false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_game, menu);
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

    private void setFragment(Fragment f, boolean addToBackStack){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(addToBackStack){
            ft.replace(R.id.fragment_container,f, f.getTag()).addToBackStack(null);
        } else {
            ft.replace(R.id.fragment_container, f, f.getTag());
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() <= 0){
            super.onBackPressed();
        }
        else{
           getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onExitPressed() {
        Toast.makeText(this, "Exit Pressed", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onOptionsPressed() {
        setFragment(new OptionFragment(), true);
        Toast.makeText(this, "Options Pressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayPressed() {
        setFragment(new GameFragment(), true);
    }

    @Override
    public void onDesignerPressed() {
        Intent getChatScreen = new Intent(this, DesignerActivity.class);

        startActivity(getChatScreen);
        //finish();
        Toast.makeText(this, "Designer Pressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOptionInteraction(Uri uri) {
        //placeholder
        int i = 3 +4;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        int i = 32;
    }
}
