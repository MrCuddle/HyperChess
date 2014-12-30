package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class GameBoard {

    Tile[][] tiles;

    public GameBoard(Context context){
        tiles = new Tile[10][10];

        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                tiles[i][j] = new Tile(context, i*200, j*200);

        MovePattern pattern = new MovePattern();
        pattern.AddDirection(MovePattern.Direction.RIGHT);
        pattern.AddDirection(MovePattern.Direction.RIGHT);
        pattern.AddDirection(MovePattern.Direction.DOWN);
        pattern.AddDirection(MovePattern.Direction.DOWN);

        pattern.HighlightTiles(tiles);

    }

    public void Draw(Canvas c){
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                tiles[i][j].Draw(c);

    }

}
