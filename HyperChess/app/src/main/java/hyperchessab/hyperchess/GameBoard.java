package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class GameBoard {

    Tile[][] tiles;
    GamePiece testPiece;
    Obstacle testObstacle;
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

        testPiece = new GamePiece(context, 3, 3, this);
        testObstacle = new Obstacle(context, 3, 1);
        tiles[3][1].occupied = true;
    }

    public GameBoard(Context context, int width, int height){
        Width = width;
        Height = height;
        tiles = new Tile[width][height];
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                tiles[i][j] = new Tile(context, i*TileSize, j*TileSize);
    }

    public Tile GetTile(int x, int y){
        if(x > Width - 1 || x < 0 || y > Height - 1 || y < 0)
            return null;
        else
            return tiles[x][y];
    }

    public void Update(double dt){
        testPiece.Update(dt);
    }

    public void Draw(Canvas c){
        for(int i = 0; i < Width; i++)
            for(int j = 0; j < Height; j++)
                tiles[i][j].Draw(c);

        testPiece.Draw(c);
        testObstacle.Draw(c);
    }

}
