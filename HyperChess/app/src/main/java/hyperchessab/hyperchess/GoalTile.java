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
        shape = context.getResources().getDrawable(R.drawable.flag_shape);
        shape.setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
        shape.setBounds(x, y, x + 200, y + 200);
        owner = player;
    }
}
