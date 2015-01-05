package hyperchessab.hyperchess;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Piece1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Piece1Fragment extends Fragment implements Designer.DesignerListener{

    int healthspinnerpoints;
    int rangespinnerpoints;

    Button reset;
    Spinner healthspinner;
    Spinner rangespinner;


    Player player;
    Designer designer;
    DesignerView designerView;

    GameManager.SavePiece savePiece;

    private View.OnClickListener buttonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            onButtonPressed(v.getId());
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Piece1Fragment.
     */
    public static Piece1Fragment newInstance(int id) {
        Piece1Fragment fragment = new Piece1Fragment();
        fragment.player = GameManager.GetUser();
        fragment.savePiece = GameManager.GetSavePiece(id);

        return fragment;
    }

    public Piece1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_piece1, container, false);

        reset = (Button) v.findViewById(R.id.Piece1Fragment_resetbtn);
        reset.setOnClickListener(buttonListener);

        EditText et = (EditText)v.findViewById(R.id.Piece1Fragment_pieceName);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    onEditPieceName(v);
                    return true;
                }
                return false;
            }
        });

        healthspinner = (Spinner) v.findViewById(R.id.Piece1Fragment_lifeSpinner);
        Integer[] healthitems = new Integer[]{1,2,3};
        ArrayAdapter<Integer> healthadapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_expandable_list_item_1 , healthitems);
        healthspinner.setAdapter(healthadapter);

        rangespinner = (Spinner) v.findViewById(R.id.Piece1Fragment_rangeSpinner);
        Integer[] rangeitems = new Integer[]{1,2,3};
        ArrayAdapter<Integer> rangeadapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_expandable_list_item_1 , rangeitems);
        rangespinner.setAdapter(rangeadapter);

        rangespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int temp = position;
                if(rangespinnerpoints != temp){
                   temp -= rangespinnerpoints;
                   rangespinnerpoints = temp;
                   UpdateRangePoints();
                   rangespinnerpoints = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        healthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int temp = position;
                if(healthspinnerpoints != temp){
                    temp -= healthspinnerpoints;
                    healthspinnerpoints = temp;
                    UpdateHealthPoints();
                    healthspinnerpoints = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        designerView = (DesignerView)v.findViewById(R.id.Piece1Fragment_designerview);
        designer = designerView.GetDesigner();
        designer.SetListener(this);
        designer.SetPattern(savePiece.pattern);
        UpdateActionBarTitle();
        return v;
    }

    private void UpdateRangePoints()
    {
        player.points -= rangespinnerpoints;
        savePiece.cost += rangespinnerpoints;
        UpdateActionBarTitle();
    }

    private void UpdateHealthPoints(){
        player.points -= healthspinnerpoints;
        savePiece.cost += healthspinnerpoints;
        UpdateActionBarTitle();
    }

    private void UpdateActionBarTitle(){
        getActivity().setTitle(player.name + savePiece.name + "                              Points left: " + player.points);
    }

    private void onEditPieceName(TextView v){
        savePiece.name = v.getText().toString();
    }

    public void onButtonPressed(int id) {
        switch (id) {
            case R.id.Piece1Fragment_resetbtn:
                designerView.ResetDesigner();
                UpdateActionBarTitle();
                break;
        }

    }

    @Override
    public void OnDesignerInteraction() {
        player.points--;
        savePiece.cost++;
        savePiece.pattern = designer.GetPattern();
        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable(){
            @Override
            public void run() {
                UpdateActionBarTitle();
            }
        });
    }
}
