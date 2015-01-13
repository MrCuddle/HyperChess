package hyperchessab.hyperchess;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Piece1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Piece1Fragment extends Fragment implements Designer.DesignerListener{

    Piece1Listener listener;

    int healthspinnerpoints;
    int rangespinnerpoints;

    Button reset;
    Button finish;
    Spinner healthspinner;
    Spinner rangespinner;

    Player player;
    Designer designer;
    DesignerView designerView;

    GameManager.SavePiece currentPiece;
    int currentPieceIndex;

    private PieceState[] pieces = new PieceState[4];

    private View.OnClickListener buttonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            OnButtonPressed(v.getId());
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Piece1Fragment.
     */
    public static Piece1Fragment newInstance(int startPieceIndex, Piece1Listener listener) {
        Piece1Fragment fragment = new Piece1Fragment();
        fragment.player = GameManager.GetUser();
        fragment.currentPiece = GameManager.GetSavePiece(startPieceIndex);
        fragment.listener = listener;
        fragment.currentPieceIndex = startPieceIndex;
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

        finish = (Button) v.findViewById(R.id.Piece1Fragment_finishbtn);
        finish.setOnClickListener(buttonListener);

        reset = (Button) v.findViewById(R.id.Piece1Fragment_resetbtn);
        reset.setOnClickListener(buttonListener);

        EditText et = (EditText)v.findViewById(R.id.Piece1Fragment_pieceName);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    OnEditPieceName(v);
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    v.setText("");
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


                designer.SetpieceDrawableHP(healthspinnerpoints + 1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        designerView = (DesignerView)v.findViewById(R.id.Piece1Fragment_designerview);
        designer = designerView.GetDesigner();
        designer.SetListener(this);
        designer.SetPattern(currentPiece.pattern, currentPieceIndex);
        UpdateActionBarTitle();
        return v;
    }

    private void UpdateRangePoints()
    {
        player.points -= rangespinnerpoints;
        currentPiece.cost += rangespinnerpoints;
        UpdateActionBarTitle();
    }

    private void UpdateHealthPoints(){
        player.points -= healthspinnerpoints;
        currentPiece.cost += healthspinnerpoints;
        UpdateActionBarTitle();
    }

    private void UpdateActionBarTitle(){
        getActivity().setTitle(player.name + currentPiece.name + "                              Points left: " + player.points);
    }

    private void OnEditPieceName(TextView v){
        currentPiece.name = v.getText().toString();
        listener.OnPieceNameChange(currentPieceIndex, v.getText().toString());
    }

    public void OnButtonPressed(int id) {
        switch (id) {
            case R.id.Piece1Fragment_resetbtn:
                currentPiece.pattern = new MovePattern();
                designer.SetPattern(currentPiece.pattern, currentPieceIndex);
                //designerView.ResetDesigner();
                UpdateActionBarTitle();
                break;
            case R.id.Piece1Fragment_finishbtn:
                
                break;
        }

    }

    public void ChangeCurrentPiece(int index){
        if (index < Settings.differentPieces && index >= 0){
            SavePiece(designer.GetPattern(), healthspinnerpoints, rangespinnerpoints, index);
            currentPiece = GameManager.GetSavePiece(index);
            currentPieceIndex = index;
            designer.SetPattern(currentPiece.pattern, index);
        }
    }

    @Override
    public void OnDesignerInteraction() {
        player.points--;
        currentPiece.cost++;
        currentPiece.pattern = designer.GetPattern();
        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable(){
            @Override
            public void run() {
                UpdateActionBarTitle();
            }
        });
    }

    @Override
    public void OnChangedPattern() {
        designerView.invalidate();
    }

    public interface Piece1Listener{
        public void OnFinishedDesigning(PieceState[] result);
        public void OnPieceNameChange(int index, String name);
    }

    private void SavePiece(MovePattern pattern, int health, int range, int id){
        PieceState piece = new PieceState();
        piece.attackRange = range;
        piece.HP = health;
        piece.shapeType = id;
        ArrayList<MovePattern> newPatterns = new ArrayList<>();
        //Init patterlist
        for (int i = 0; i < 4; i++) {
            newPatterns.add(new MovePattern());
        }

        //For every pattern in newPattern, for every move in pattern
        for (int p = 0; p < 4; p++) {
            for (int m = 0; m < pattern.Size(); m++) {
                int newDir = (pattern.Get(m) + p) % 4;
                newPatterns.get(p).AddDirection(newDir);
            }
        }

        piece.movePatterns = newPatterns;
        pieces[id] = piece;
    }

}
