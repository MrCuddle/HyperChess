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

    public static final int TileSize = 200;

    public GameBoard(Context context){
        tiles = new Tile[10][10];

        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                tiles[i][j] = new Tile(context, i*TileSize, j*TileSize);

        testPiece = new GamePiece(context, 3, 3, this);
        testObstacle = new Obstacle(context, 2, 2);
    }

    public GameBoard(Context context, int width, int height){
        tiles = new Tile[width][height];
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                tiles[i][j] = new Tile(context, i*TileSize, j*TileSize);
    }

    public Tile GetTile(int x, int y){
        if(x > 9 || x < 1 || y > 9 || y < 1)
            return null;
        else
            return tiles[x][y];
    }

    public void Update(double dt){
        testPiece.Update(dt);
    }

    public void Draw(Canvas c){
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                tiles[i][j].Draw(c);

        testPiece.Draw(c);
        testObstacle.Draw(c);
    }

}
