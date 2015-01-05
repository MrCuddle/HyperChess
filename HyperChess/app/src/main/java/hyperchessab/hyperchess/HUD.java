package hyperchessab.hyperchess;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Simon on 2015-01-05.
 */
public class HUD {
    Paint p;
    Rect scoreBounds;
    String currentScore;
    public HUD(){
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setTextSize(200);
        scoreBounds = new Rect();
        currentScore = "0 - 0";
        p.getTextBounds(currentScore, 0, currentScore.length(), scoreBounds);
    }

    public void Update(){
        currentScore = GameData.teamOneScore + " - " + GameData.teamTwoScore;
        p.getTextBounds(currentScore, 0, currentScore.length(), scoreBounds);
    }

    public void Draw(Canvas c){
        c.drawText(currentScore,
                c.getClipBounds().centerX() - scoreBounds.width() / 2,
                c.getClipBounds().centerY() - c.getClipBounds().height()/2 + 140, p);
    }

}
