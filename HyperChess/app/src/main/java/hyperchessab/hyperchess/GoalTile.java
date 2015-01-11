package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;

/**
 * Created by Perlwin on 02/01/2015.
 */
public class GoalTile extends Tile{
    Player owner;
    public GoalTile(Context context, int x, int y, Player player){
        shape = context.getResources().getDrawable(R.drawable.tile_shape_goal);
        shape.setColorFilter(Color.argb(255,0,0,255), PorterDuff.Mode.SRC);
        shape.setBounds(x, y, x + GameBoard.TileSize, y + GameBoard.TileSize);
        owner = player;
    }
}
