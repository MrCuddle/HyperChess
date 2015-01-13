package hyperchessab.hyperchess;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

    Designer designer;
    DesignerView designerView;

    PieceState currentPiece;
    MovePattern currentPattern = new MovePattern();
    int currentPieceIndex = 0;
    int playerPoints = Settings.playerPoints;

    //Filled in by the method that checks if there are piece that are not finished yet
    int indexOfANonFinishedPiece = 0;

    private PieceState[] pieces = new PieceState[Settings.differentPieces];

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
    public static Piece1Fragment newInstance(Piece1Listener listener) {
        Piece1Fragment fragment = new Piece1Fragment();
        fragment.listener = listener;

        for (int i = 0; i < Settings.differentPieces; i++) {
            fragment.pieces[i] = null;
        }
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
                UpdateRangePoints(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        healthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpdateHealthPoints(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        designerView = (DesignerView)v.findViewById(R.id.Piece1Fragment_designerview);
        designer = designerView.GetDesigner();
        designer.SetListener(this);
        designer.SetPattern(currentPattern, currentPieceIndex);


        return v;
    }

    private void UpdateRangePoints(int spinnerValue)
    {
        int temp = spinnerValue;
        if(rangespinnerpoints != temp){
            temp -= rangespinnerpoints;
            rangespinnerpoints = temp;
            playerPoints -= rangespinnerpoints;
            rangespinnerpoints = spinnerValue;
            if(currentPiece == null){
                currentPiece = new PieceState();
                currentPiece.attackRange = 1;
                currentPiece.HP = 1;
            }
            currentPiece.attackRange = rangespinnerpoints + 1;
        }
    }

    private void UpdateHealthPoints(int spinnerValue){
        int temp = spinnerValue;
        if(healthspinnerpoints != temp){
            temp -= healthspinnerpoints;
            healthspinnerpoints = temp;
            playerPoints -= healthspinnerpoints;
            healthspinnerpoints = spinnerValue;
            if(currentPiece == null){
                currentPiece = new PieceState();
                currentPiece.attackRange = 1;
                currentPiece.HP = 1;
            }
            currentPiece.HP = healthspinnerpoints + 1;
        }

        designer.SetpieceDrawableHP(healthspinnerpoints + 1);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (Piece1Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void OnButtonPressed(int id) {
        switch (id) {
            case R.id.Piece1Fragment_resetbtn:
                currentPattern = new MovePattern();
                designer.SetPattern(currentPattern, currentPieceIndex);
                break;
            case R.id.Piece1Fragment_finishbtn:
                if(AllPiecesDesigned()){
                    listener.OnFinishedDesigning(pieces);
                } else {

                    ChangeTab(indexOfANonFinishedPiece);
                }

                break;
        }
    }

    public void ChangeCurrentPiece(int index){
        if (index < Settings.differentPieces && index >= 0){
            if(currentPiece != null){
                SavePiece(designer.GetPattern(), currentPiece.HP, currentPiece.attackRange, currentPieceIndex);
            }

            currentPiece = pieces[index];
            if(currentPiece != null){
                designer.SetPattern(currentPiece.movePatterns.get(0), index);
                healthspinner.setSelection(currentPiece.HP - 1);
                rangespinner.setSelection(currentPiece.attackRange - 1);
            } else {
                designer.SetPattern(null, index);
                healthspinner.setSelection(0);
                rangespinner.setSelection(0);
            }

            currentPieceIndex = index;


        }
    }

    @Override
    public void OnDesignerInteraction() {
        playerPoints--;
        currentPattern = designer.GetPattern();
        if(currentPiece == null){
            currentPiece = new PieceState();
            currentPiece.attackRange = 1;
            currentPiece.HP = 1;
        }
    }

    @Override
    public void OnChangedPattern() {
        designerView.invalidate();
    }

    public interface Piece1Listener{
        public void OnFinishedDesigning(PieceState[] result);
        public void RequestTabChange(int tab);
    }

    private void SavePiece(MovePattern pattern, int health, int range, int id){
        PieceState piece = new PieceState();
        piece.attackRange = range;
        piece.HP = health;
        piece.shapeType = id;
        ArrayList<MovePattern> newPatterns = new ArrayList<>();
        //Init patternlist
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

    private boolean AllPiecesDesigned(){
        boolean allDesigned = true;
        boolean allHaveMovement = true;
        if(currentPiece != null){
            SavePiece(designer.GetPattern(), currentPiece.HP, currentPiece.attackRange, currentPieceIndex);
        }

        for (int i = 0; i < pieces.length; i++) {
            if(pieces[i] == null){
                allDesigned = false;
                indexOfANonFinishedPiece = i;
                break;
            }
        }

        if(allDesigned){
            for (int i = 0; i < pieces.length; i++) {
                if(pieces[i].movePatterns.get(0).Size() == 0){
                    allHaveMovement = false;
                    indexOfANonFinishedPiece = i;
                    break;
                }
            }
        } else {
            return false;
        }

        return allDesigned && allHaveMovement;
    }

    private void ChangeTab(int position){
        listener.RequestTabChange(position);
    }

}
