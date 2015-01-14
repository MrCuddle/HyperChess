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
        int r = Color.red(player.GetPrimaryColor());
        int g = Color.green(player.GetPrimaryColor());
        int b = Color.blue(player.GetPrimaryColor());
        shape = context.getResources().getDrawable(R.drawable.tile_shape_goal);
        shape.setColorFilter(Color.argb(255, Math.max(r - 30, 0), Math.max(g - 30, 0), Math.max(b - 30, 0)), PorterDuff.Mode.SRC);
        shape.setBounds(x, y, x + GameBoard.TileSize, y + GameBoard.TileSize);
        owner = player;
    }
}
