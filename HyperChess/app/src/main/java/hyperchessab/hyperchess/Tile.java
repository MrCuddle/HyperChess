package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

/**
* Created by Perlwin on 29/12/2014.
        */
public class Tile {

    protected Drawable shape;
    //public boolean occupied = false;
    public GameObject occupier;


    public Tile(){}

    public Tile(Context context, int x, int y){

        if(((x + y)/GameBoard.TileSize) % 2 == 1){
            shape = context.getResources().getDrawable(R.drawable.tile_shape_dark);
        } else {
            shape = context.getResources().getDrawable(R.drawable.tile_shape);
        }
        shape.setBounds(x,y,x + GameBoard.TileSize,y+GameBoard.TileSize);
    }

    public void Draw(Canvas c){
        shape.draw(c);
    }

    public void Highlight(){
        shape.setColorFilter(0x00ff00, PorterDuff.Mode.MULTIPLY );

    }

}
