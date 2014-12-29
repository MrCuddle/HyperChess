package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

/**
* Created by Perlwin on 29/12/2014.
        */
public class Tile {

    Drawable shape;

    public Tile(){}

    public Tile(Context context, int x, int y){
        shape = context.getResources().getDrawable(R.drawable.tile_shape);
        shape.setBounds(x,y,x + 200,y+200);
    }

    public void Draw(Canvas c){
        shape.draw(c);
    }

    public void Highlight(){
        shape.setColorFilter(0x00ff00, PorterDuff.Mode.MULTIPLY );

    }

}
