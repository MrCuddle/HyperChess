package hyperchessab.hyperchess;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Piece1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Piece1Fragment extends Fragment implements Designer.DesignerListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int healthspinnerpoints;
    int rangespinnerpoints;

    Button reset;
    Spinner healthspinner;
    Spinner rangespinner;


    Player player;
    Designer designer;
    DesignerView designerView;

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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Piece1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Piece1Fragment newInstance(String param1, String param2) {
        Piece1Fragment fragment = new Piece1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.player = GameManager.GetUser();
        return fragment;
    }

    public Piece1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_piece1, container, false);

        reset = (Button) v.findViewById(R.id.Piece1Fragment_resetbtn);
        reset.setOnClickListener(buttonListener);


        healthspinner = (Spinner) v.findViewById(R.id.Piece1Fragment_lifeSpinner);
        Integer[] healthitems = new Integer[]{1,2,3};
        ArrayAdapter<Integer> healthadapter = new ArrayAdapter<Integer>(getActivity() , android.R.layout.simple_expandable_list_item_1 , healthitems);
        healthspinner.setAdapter(healthadapter);

        rangespinner = (Spinner) v.findViewById(R.id.Piece1Fragment_rangeSpinner);
        Integer[] rangeitems = new Integer[]{1,2,3};
        ArrayAdapter<Integer> rangeadapter = new ArrayAdapter<Integer>(getActivity() , android.R.layout.simple_expandable_list_item_1 , rangeitems);
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
        UpdateActionBarTitle();
        return v;
    }

    private void UpdateRangePoints()
    {
        player.points -= rangespinnerpoints;
        UpdateActionBarTitle();
    }

    private void UpdateHealthPoints(){
        player.points -= healthspinnerpoints;
        UpdateActionBarTitle();
    }

    private void UpdateActionBarTitle(){
        getActivity().setTitle(player.name + "                              Points left: " + player.points);
        healthspinner.setSelection(0);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int id) {
        switch (id) {
            case R.id.Piece1Fragment_resetbtn:
                designerView.ResetDesigner();
                UpdateActionBarTitle();
                break;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void OnDesignerInteraction() {
        player.points--;

        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable(){
            @Override
            public void run() {
                UpdateActionBarTitle();
            }
        });
    }
}
