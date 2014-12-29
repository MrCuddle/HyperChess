package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class GamePiece {

    Drawable shape;
    boolean selected = false;

    public GamePiece(Context context, float x, float y){
        shape = context.getResources().getDrawable(R.drawable.piece_shape_1);
        shape.setBounds((int)x, (int)y, (int)x + GameBoard.TileSize, (int)y + GameBoard.TileSize);
    }

    public void Update(double dt){
        if(InputData.Clicked){
            if(InputData.ClickPoint.x >= shape.getBounds().left && InputData.ClickPoint.x <= shape.getBounds().right && InputData.ClickPoint.y >= shape.getBounds().top && InputData.ClickPoint.y <= shape.getBounds().bottom){
                shape.setColorFilter(Color.WHITE, PorterDuff.Mode.ADD);
            } else {
                shape.setColorFilter(Color.BLUE, PorterDuff.Mode.ADD);

            }
        }
    }

    public void Draw(Canvas c){
        shape.draw(c);
    }

}
