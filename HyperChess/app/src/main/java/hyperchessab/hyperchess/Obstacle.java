package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class Obstacle {

    Drawable shape;


    public Obstacle(Context context, float x, float y){
        shape = context.getResources().getDrawable(R.drawable.obstacle_shape);
        shape.setBounds((int)x, (int)y, (int)x + GameBoard.TileSize, (int)y + GameBoard.TileSize);
    }

    public void Draw(Canvas c){
        shape.draw(c);

    }

}
