package hyperchessab.hyperchess;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class Game {

    public enum GameState { Moving, Attacking }
    public GameState currentGameState = GameState.Moving;

    //Flyttas till PlayerManager senare
    public ArrayList<Player> players = new ArrayList<Player>();
    int currentPlayer;

    GameBoard board;
    Camera camera;
    HUD hud;
    public boolean online;
    public Player localPlayer;
    public int localPlayerNumber;
    public String gameId;
    ChildEventListener dbListener;
    Firebase fb;

    Context context;

    public Object sync = new Object();

    private Turn thisTurn;

    public Game(Context context, Camera camera, boolean online, int player, String gameId){
        this.context = context;
        players.add(new Player(0));
        players.add(new Player(1));
        players.get(0).SetPrimaryColor(Color.argb(255,147,79,236));
        players.get(0).SetSecondaryColor(Color.argb(255,156,93,238));
        players.get(0).SetTertiaryColor(Color.argb(255,179,131,242));
        players.get(1).SetPrimaryColor(Color.argb(255,228,21,21));
        players.get(1).SetSecondaryColor(Color.argb(255,234,39,39));
        players.get(1).SetTertiaryColor(Color.argb(255,239,90,90));

        this.online = online;
        localPlayer = players.get(player);
        this.gameId = gameId;
        localPlayerNumber = player;

        board = new GameBoard(context,this);
        this.camera = camera;
        camera.setBounds(board.Width * GameBoard.TileSize, board.Height * GameBoard.TileSize);
        currentPlayer = 0;
        hud = new HUD();

        //UpdateActionBarText();

        //If playing online, register to listen for turn data
        if(online){
            InitFirebase();
        }
    }

    public void InitFirebase(){
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
    }

    public void RemoveListeners(){
        if(online){
            fb.removeEventListener(dbListener);
        }
    }

    public void Update(double dt){
        board.Update(dt);

        switch(currentGameState){
            case Moving:
                break;
            case Attacking:
                break;
        }

        //hud.Update();
    }

    public void IncrementCurrentPlayer(){
        if(online && MyTurn()){
            //Send this turn's move to the database!
            Firebase fb = new Firebase(DatabaseManager.URL).child("games").child(gameId).child("turn");
            fb.setValue(thisTurn);
        }

        currentPlayer = (currentPlayer + 1) % 2;
        //hud.SetCurrentPlayer(currentPlayer);
        UpdateActionBarText();
    }

    public void InitializeActionBar(){
        UpdateActionBarText();
    }
    private void UpdateActionBarText(){
        final String scoreAsString = (currentPlayer == 0) ?
                "[" + GameData.teamOneScore + "]" + " - " + GameData.teamTwoScore :
                    GameData.teamOneScore + " - " + "[" + GameData.teamTwoScore + "]";
        ((ActionBarActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView view = (TextView)(((ActionBarActivity)context).findViewById(R.id.action_bar_score_text_view));
                view.setText(scoreAsString);
            }
        });


    }

    public void Draw(Canvas c){
        c.setMatrix(camera.getTransform());
        //c.drawColor(Color.BLACK);
        board.Draw(c);
        //c.setMatrix(new Matrix());
        //hud.Draw(c);
    }

    public boolean MyTurn(){
        return players.get(currentPlayer) == localPlayer;
    }


    public void RecordMove(int startX, int startY, int endX, int endY){
        thisTurn = new Turn(startX,startY,endX,endY,-1,-1,localPlayerNumber);
    }

    public void RecordAttack(int x, int y){
        thisTurn.setAttackX(x);
        thisTurn.setAttackY(y);
    }

    public String GameStateToJSON(){



        GameStatePackage gsp = new GameStatePackage();
        gsp.gameID = gameId;
        gsp.currentPlayer = currentPlayer;
        gsp.online = online;
        gsp.player1Points = GameData.teamOneScore;
        gsp.player2Points = GameData.teamTwoScore;
        gsp.playerNumber = localPlayerNumber;
        gsp.pieces = board.GetPieceStates();
        gsp.currentGameState = currentGameState;

        Gson gson = new Gson();
        String json = gson.toJson(gsp);

        return json;

    }

    public void LoadGameStateFromJSON(String json){

        Gson gson = new Gson();
        GameStatePackage gsp = gson.fromJson(json,GameStatePackage.class);

        online = gsp.online;
        currentPlayer = gsp.currentPlayer;
        localPlayerNumber = gsp.playerNumber;
        currentGameState = gsp.currentGameState;
        board.AddObjects(gsp.pieces);
        gameId = gsp.gameID;
        GameData.teamOneScore = gsp.player1Points;
        GameData.teamTwoScore = gsp.player2Points;

        hud.SetCurrentPlayer(currentPlayer);

        if(online){
            InitFirebase();
        }

    }
}
