package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class GameBoard {

    Tile[][] tiles;
    ArrayList<GamePiece> pieces = new ArrayList<GamePiece>();
    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    Flag flag;
    Game game;

    public Game getGame(){
        return game;
    }

    public static final int TileSize = 200;
    int Width = 10;
    int Height = 10;

    public GameBoard(Context context, Game game){
        this.game = game;
        tiles = new Tile[Width][Height];

        for(int i = 0; i < Width; i++)
            for(int j = 0; j < Height; j++)
                tiles[i][j] = new Tile(context, i*TileSize, j*TileSize);

        GamePiece gp = new GamePiece(context, 3, 3, this);
        gp.SetOwner(game.players.get(0));
        gp.SetAttackRange(1);
        pieces.add(gp);
        tiles[3][3].occupier = gp;

        gp = new GamePiece(context, 7, 0, this);
        gp.SetAttackRange(1);
        gp.SetOwner(game.players.get(1));
        pieces.add(gp);
        tiles[7][0].occupier = gp;

        gp = new GamePiece(context, 4, 4, this);
        gp.SetAttackRange(1);
        gp.SetOwner(game.players.get(0));
        pieces.add(gp);
        tiles[4][4].occupier = gp;


        Obstacle o = new Obstacle(context, 3, 1);
        obstacles.add(o);
        tiles[3][1].occupier = o;

        flag = new Flag(context,6,6);
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
