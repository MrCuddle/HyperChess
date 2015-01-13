package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class GameBoard {

    Tile[][] tiles;
    ArrayList<GamePiece> pieces = new ArrayList<GamePiece>();
    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

    //Start pos + Piece prototype test
    ArrayList<Point> teamOneStartPos, teamTwoStartPos;
    ArrayList<GamePiece> piecePrototypes;

    Flag flag;
    Game game;

    Context context;

    public Game getGame(){
        return game;
    }

    public static final int TileSize = 200;
    int Width = 11;
    int Height = 15;

    public GameBoard(Context context, Game game){
        this.game = game;
        this.context = context;
        tiles = new Tile[Width][Height];

        teamOneStartPos = new ArrayList<Point>();
        teamTwoStartPos = new ArrayList<Point>();

        for(int i = 0; i < Width; i++)
            for(int j = 0; j < Height; j++)
                tiles[i][j] = new Tile(context, i*TileSize, j*TileSize);
    }

    public ArrayList<PieceState> GetPieceStates(){
        ArrayList<PieceState> states = new ArrayList<PieceState>();
        for(int i = 0; i < pieces.size(); ++i){
            states.add(pieces.get(i).GetPieceState());
        }
        return states;
    }

    public void AddObjects(ArrayList<PieceState> pieces){

        AddStaticObjects();

        for(int i = 0; i < pieces.size(); i++){
            GamePiece gp = new GamePiece(context, pieces.get(i), this);
            this.pieces.add(gp);
            tiles[pieces.get(i).gridPosX][pieces.get(i).gridPosY].occupier = gp;
        }
    }

    public void AddObjects(){
//        GamePiece gp = new GamePiece(context, 3, 3, this);
//        gp.SetOwner(game.players.get(0));
//        gp.SetAttackRange(1);
//        gp.SetHP(3);
//        pieces.add(gp);
//        tiles[3][3].occupier = gp;
//
//        gp = new GamePiece(context, 7, 0, this);
//        gp.SetAttackRange(1);
//        gp.SetOwner(game.players.get(1));
//        gp.SetHP(1);
//        pieces.add(gp);
//        tiles[7][0].occupier = gp;
//
//        gp = new GamePiece(context, 4, 4, this);
//        gp.SetAttackRange(1);
//        gp.SetOwner(game.players.get(0));
//        gp.SetHP(2);
//        pieces.add(gp);
//        tiles[4][4].occupier = gp;

        AddStartPositions();
        AddPrototypes();

        for(int i = 0; i < piecePrototypes.size(); i++)
        {
            GamePiece p1 = new GamePiece(piecePrototypes.get(i));
            GamePiece p2 = new GamePiece(piecePrototypes.get(i));
            p1.SetOwner(game.players.get(0));
            p2.SetOwner(game.players.get(0));

            p1.SetPosition(teamOneStartPos.get(i).x,teamOneStartPos.get(i).y);
            p1.SetStartPosition(teamOneStartPos.get(i).x,teamOneStartPos.get(i).y);
            tiles[teamOneStartPos.get(i).x][teamOneStartPos.get(i).y].occupier = p1;
            p2.SetPosition(teamOneStartPos.get(teamOneStartPos.size()-1- i).x,teamOneStartPos.get(teamOneStartPos.size()-1-i).y);
            p2.SetStartPosition(teamOneStartPos.get(teamOneStartPos.size()-1- i).x,teamOneStartPos.get(teamOneStartPos.size()-1-i).y);
            tiles[teamOneStartPos.get(teamOneStartPos.size()-1- i).x][teamOneStartPos.get(teamOneStartPos.size()-1-i).y].occupier = p2;

            GamePiece p3 = new GamePiece(piecePrototypes.get(i));
            GamePiece p4 = new GamePiece(piecePrototypes.get(i));
            p3.SetOwner(game.players.get(1));
            p4.SetOwner(game.players.get(1));

            p3.SetPosition(teamTwoStartPos.get(i).x,teamTwoStartPos.get(i).y);
            p3.SetStartPosition(teamTwoStartPos.get(i).x,teamTwoStartPos.get(i).y);
            tiles[teamTwoStartPos.get(i).x][teamTwoStartPos.get(i).y].occupier = p3;
            p4.SetPosition(teamTwoStartPos.get(teamTwoStartPos.size()-1-i).x,teamTwoStartPos.get(teamTwoStartPos.size()-1-i).y);
            p4.SetStartPosition(teamTwoStartPos.get(teamTwoStartPos.size()-1-i).x,teamTwoStartPos.get(teamTwoStartPos.size()-1-i).y);
            tiles[teamTwoStartPos.get(teamTwoStartPos.size()-1-i).x][teamTwoStartPos.get(teamTwoStartPos.size()-1-i).y].occupier = p4;

            pieces.add(p1);
            pieces.add(p2);
            pieces.add(p3);
            pieces.add(p4);
        }

        AddStaticObjects();

    }

    private void AddStartPositions(){
        teamOneStartPos.add(new Point(0, 3));
        teamOneStartPos.add(new Point(1, 4));
        teamOneStartPos.add(new Point(2, 3));

        teamOneStartPos.add(new Point(4, 3));
        teamOneStartPos.add(new Point(6, 3));

        teamOneStartPos.add(new Point(8, 3));
        teamOneStartPos.add(new Point(9, 4));
        teamOneStartPos.add(new Point(10, 3));

        teamTwoStartPos.add(new Point(0, 11));
        teamTwoStartPos.add(new Point(1, 10));
        teamTwoStartPos.add(new Point(2, 11));

        teamTwoStartPos.add(new Point(4, 11));
        teamTwoStartPos.add(new Point(6, 11));

        teamTwoStartPos.add(new Point(8, 11));
        teamTwoStartPos.add(new Point(9, 10));
        teamTwoStartPos.add(new Point(10, 11));
    }

    public ArrayList<Point> GetStartPositions(int teamId){
        if(teamId == 0)
            return teamOneStartPos;
        else if(teamId == 1)
            return teamTwoStartPos;
        else
            return null;
    }

    private void AddPrototypes(){
        GamePiece prototype1 = new GamePiece(context, this, 0);
        prototype1.SetInitHP(3);
        prototype1.SetAttackRange(1);

        GamePiece prototype2 = new GamePiece(context, this, 1);
        prototype2.SetInitHP(2);
        prototype2.SetAttackRange(2);

        GamePiece prototype3 = new GamePiece(context, this, 2);
        prototype3.SetInitHP(1);
        prototype3.SetAttackRange(2);

        GamePiece prototype4 = new GamePiece(context, this, 3);
        prototype4.SetInitHP(2);
        prototype4.SetAttackRange(4);

        piecePrototypes = new ArrayList<GamePiece>();

        piecePrototypes.add(prototype1);
        piecePrototypes.add(prototype2);
        piecePrototypes.add(prototype3);
        piecePrototypes.add(prototype4);
    }

    public void AddStaticObjects(){
        Obstacle o = new Obstacle(context, 3, 7);
        obstacles.add(o);
        tiles[3][7].occupier = o;
        o = new Obstacle(context, 3, 6);
        obstacles.add(o);
        tiles[3][6].occupier = o;
        o = new Obstacle(context, 4, 5);
        obstacles.add(o);
        tiles[4][5].occupier = o;
        o = new Obstacle(context, 5, 5);
        obstacles.add(o);
        tiles[5][5].occupier = o;

        o = new Obstacle(context, 7, 7);
        obstacles.add(o);
        tiles[7][7].occupier = o;
        o = new Obstacle(context, 7, 8);
        obstacles.add(o);
        tiles[7][8].occupier = o;
        o = new Obstacle(context, 6, 9);
        obstacles.add(o);
        tiles[6][9].occupier = o;
        o = new Obstacle(context, 5, 9);
        obstacles.add(o);
        tiles[5][9].occupier = o;

        o = new Obstacle(context, 5, 2);
        obstacles.add(o);
        tiles[5][2].occupier = o;
        o = new Obstacle(context, 6, 2);
        obstacles.add(o);
        tiles[6][2].occupier = o;
        o = new Obstacle(context, 7, 2);
        obstacles.add(o);
        tiles[7][2].occupier = o;
        o = new Obstacle(context, 8, 2);
        obstacles.add(o);
        tiles[8][2].occupier = o;

        o = new Obstacle(context, 5, 12);
        obstacles.add(o);
        tiles[5][12].occupier = o;
        o = new Obstacle(context, 4, 12);
        obstacles.add(o);
        tiles[4][12].occupier = o;
        o = new Obstacle(context, 3, 12);
        obstacles.add(o);
        tiles[3][12].occupier = o;
        o = new Obstacle(context, 2, 12);
        obstacles.add(o);
        tiles[2][12].occupier = o;

        tiles[5][0] = new GoalTile(context, 5 * TileSize, 0, game.players.get(0));
        tiles[4][0] = new GoalTile(context, 4 * TileSize, 0, game.players.get(0));
        tiles[6][0] = new GoalTile(context, 6 * TileSize, 0, game.players.get(0));

        tiles[5][Height-1] = new GoalTile(context, 5 * TileSize, (Height-1) * TileSize, game.players.get(1));
        tiles[4][Height-1] = new GoalTile(context, 4 * TileSize, (Height-1) * TileSize, game.players.get(1));
        tiles[6][Height-1] = new GoalTile(context, 6 * TileSize, (Height-1) * TileSize, game.players.get(1));

        flag = new Flag(context,5,7);
    }

    public boolean IsOnFlag(int posX, int posY){
        return posX == flag.gridPosX && posY == flag.gridPosY;
    }

    public Flag GetFlag(){
        return flag;
    }

    public GameBoard(Context context, int width, int height){
        Width = width;
        Height = height;
        tiles = new Tile[width][height];
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                tiles[i][j] = new Tile(context, i*TileSize, j*TileSize);
    }

    public void Select(GamePiece selected){
        for(GamePiece gp : pieces){
            if(gp != selected)
                gp.Deselect();
        }
    }

    public Tile GetTile(int x, int y){
        if(x > Width - 1 || x < 0 || y > Height - 1 || y < 0)
            return null;
        else
            return tiles[x][y];
    }

    public void Update(double dt){
        for(int i = 0; i < pieces.size(); i++){
            pieces.get(i).Update(dt);
        }
        flag.Update(dt);
    }

    public void Draw(Canvas c){
        for(int i = 0; i < Width; i++)
            for(int j = 0; j < Height; j++)
                tiles[i][j].Draw(c);

        for(GamePiece gp : pieces){
            gp.Draw(c);
        }
        for(Obstacle o : obstacles){
            o.Draw(c);
        }
        flag.Draw(c);
    }

}
