package hyperchessab.hyperchess;

import android.content.Context;

/**
 * Created by jespe_000 on 2014-12-30.
 */
public class Designer extends Game {

    public Designer(Context context, Camera camera){
        super(context, camera);
        board = new GameBoard(context);
        this.camera = camera;
    }



}
