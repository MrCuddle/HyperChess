package hyperchessab.hyperchess;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import android.os.Handler;
import android.os.Looper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Piece1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Piece1Fragment extends Fragment implements Designer.DesignerListener{

    Piece1Listener listener;

//    int healthspinnerpoints;
//    int rangespinnerpoints;

    Button reset;
    Button finish;
    Spinner healthspinner;
    Spinner rangespinner;
    TextView pointsText, pieceCostText;

    Designer designer;
    DesignerView designerView;

    ArrayAdapter<Integer> healthadapter;
    ArrayList<Integer> healthitems = new ArrayList<>();

    ArrayAdapter<Integer> rangeadapter;
    ArrayList<Integer> rangeitems = new ArrayList<>();

    PieceState currentPiece;
    MovePattern currentPattern = new MovePattern();
    int playerNumber;
    int currentPieceIndex = 0;
    int playerPoints = Settings.playerPoints;
    int pieceCost[] = new int[Settings.differentPieces];

    boolean updateHealthPoints = true, updateRangePoints = true;

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
    public static Piece1Fragment newInstance(Piece1Listener listener, int playerNumber) {
        Piece1Fragment fragment = new Piece1Fragment();
        fragment.listener = listener;
        fragment.playerNumber = playerNumber;
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

        pointsText = (TextView)v.findViewById(R.id.Piece1Fragment_points_text);
        pieceCostText = (TextView)v.findViewById(R.id.Piece1Fragment_piecepoints_text);

        for (int i = 0; i < 3; i++) {
            healthitems.add(i + 1);
            rangeitems.add(i + 1);
        }

        healthspinner = (Spinner) v.findViewById(R.id.Piece1Fragment_lifeSpinner);
        healthadapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_expandable_list_item_1 , healthitems);
        healthspinner.setAdapter(healthadapter);

        rangespinner = (Spinner) v.findViewById(R.id.Piece1Fragment_rangeSpinner);
        rangeadapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_expandable_list_item_1 , rangeitems);
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
        if(playerNumber == 1){
            designer.SetDrawableColor();
        }

        return v;
    }

    private void UpdateRangePoints(int spinnerValue)
    {
        if(currentPiece == null){
            currentPiece = new PieceState();
            currentPiece.attackRange = 1;
            currentPiece.HP = 1;
        }
        currentPiece.attackRange = spinnerValue + 1;
        CalculateCurrentPieceCost();
        UpdatePoints();
    }

    private void UpdateHealthPoints(int spinnerValue){
        if(currentPiece == null){
            currentPiece = new PieceState();
            currentPiece.attackRange = 1;
            currentPiece.HP = 1;
        }
        currentPiece.HP = spinnerValue + 1;

        designer.SetpieceDrawableHP(currentPiece.HP);
        CalculateCurrentPieceCost();
        UpdatePoints();
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
                healthspinner.setSelection(0);
                rangespinner.setSelection(0);
                currentPiece.HP = 1;
                currentPiece.attackRange = 1;
                CalculateCurrentPieceCost();
                UpdatePoints();
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
            currentPieceIndex = index;
            if(currentPiece == null) {
                currentPiece = new PieceState();
                currentPiece.movePatterns = new ArrayList<>();
                currentPiece.movePatterns.add(new MovePattern());
                currentPiece.HP = 1;
                currentPiece.attackRange = 1;
            }
            designer.SetPattern(currentPiece.movePatterns.get(0), index);
            healthspinner.setSelection(currentPiece.HP - 1);
            rangespinner.setSelection(currentPiece.attackRange - 1);

            designer.SetpieceDrawableHP(currentPiece.HP);
            UpdatePieceCost();
        }
    }

    @Override
    public void OnDesignerInteraction(boolean userInteraction) {
        if(userInteraction){

        }

        currentPattern = designer.GetPattern();
        if(currentPiece == null){
            currentPiece = new PieceState();
            currentPiece.attackRange = 1;
            currentPiece.HP = 1;
        }
        CalculateCurrentPieceCost();
        UpdatePoints();
    }

    @Override
    public boolean AllowInteraction() {
        return playerPoints > 0;
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
        piece.initHP = health;
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

    private void UpdatePoints(){
        int newPoints = 0;

        for (int i = 0; i < pieceCost.length; i++) {
            newPoints += pieceCost[i];
        }
        playerPoints = Settings.playerPoints - newPoints;
        final int temp = playerPoints;
        Handler refresh = new Handler(Looper.getMainLooper());

        int maxPossibleHealth = playerPoints + currentPiece.HP;
        UpdateHealthSpinner(maxPossibleHealth);
        int maxPossibleRange = playerPoints + currentPiece.attackRange;
        UpdateRangeSpinner(maxPossibleRange);

        refresh.post(new Runnable(){
            @Override
            public void run() {
                pointsText.setText("POINTS LEFT: " + Integer.toString(temp));
            }
        });
        UpdatePieceCost();
    }

    private void UpdatePieceCost(){
        final int tempPieceCost = pieceCost[currentPieceIndex];
        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable(){
            @Override
            public void run() {
                pieceCostText.setText("PIECE VALUE: " + Integer.toString(tempPieceCost));
            }
        });
    }

    private void CalculateCurrentPieceCost(){
        if(currentPiece != null){
            pieceCost[currentPieceIndex] = designer.GetPatternSize() + currentPiece.HP - 1 + currentPiece.attackRange - 1;
        } else {
            pieceCost[currentPieceIndex] = designer.GetPatternSize();
        }

    }

    private void UpdateHealthSpinner(int maxPossible){
        if(maxPossible <= 2){
            Handler refresh = new Handler(Looper.getMainLooper());
            healthitems.clear();
            for (int i = 0; i < maxPossible; i++) {
                healthitems.add(i + 1);
            }
            refresh.post(new Runnable(){
            @Override
            public void run() {
                healthadapter.notifyDataSetChanged();
                }
            });
        } else {
            if(healthitems.size() < 3){
                Handler refresh = new Handler(Looper.getMainLooper());
                healthitems.clear();
                for (int i = 0; i < 3; i++) {
                    healthitems.add(i + 1);
                }
                refresh.post(new Runnable(){
                    @Override
                    public void run() {
                        healthadapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    private void UpdateRangeSpinner(int maxPossible){
        if(maxPossible <= 2){
            Handler refresh = new Handler(Looper.getMainLooper());
            rangeitems.clear();
            for (int i = 0; i < maxPossible; i++) {
                rangeitems.add(i + 1);
            }
            refresh.post(new Runnable(){
                @Override
                public void run() {
                    rangeadapter.notifyDataSetChanged();
                }
            });
        } else {
            if(rangeitems.size() < 3){
                Handler refresh = new Handler(Looper.getMainLooper());
                rangeitems.clear();
                for (int i = 0; i < 3; i++) {
                    rangeitems.add(i + 1);
                }
                refresh.post(new Runnable(){
                    @Override
                    public void run() {
                        rangeadapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

}
