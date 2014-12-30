package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class Obstacle {

    Drawable shape;

    public Obstacle(Context context, int x, int y){
        shape = context.getResources().getDrawable(R.drawable.obstacle_shape);
        shape.setBounds(x*GameBoard.TileSize, y*GameBoard.TileSize,
                x*GameBoard.TileSize + GameBoard.TileSize, y*GameBoard.TileSize + GameBoard.TileSize);
    }

    public void Draw(Canvas c){
        shape.draw(c);

    }

}
