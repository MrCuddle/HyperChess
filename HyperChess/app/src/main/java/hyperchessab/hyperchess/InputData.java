package hyperchessab.hyperchess;

import android.graphics.Point;

/**
 * Created by Simpa on 2014-12-29.
 */
public class InputData {
    public static Point ClickPoint;
    static boolean Clicked;
    public InputData(){

    }

    public static void Clear(){
        ClickPoint = null;
        Clicked = false;
    }
}
