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
    int currentPlayer;
    public HUD(){
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setTextSize(150);
        scoreBounds = new Rect();
        currentScore = "0 - 0";
        currentPlayer = 0;
        p.getTextBounds(currentScore, 0, currentScore.length(), scoreBounds);
    }

    public void SetCurrentPlayer(int playerId){
        currentPlayer = playerId;
    }

    public void Update(){
        if(currentPlayer == 0)
            currentScore = "[" + GameData.teamOneScore + "]" + " - " + GameData.teamTwoScore;
        else
            currentScore = GameData.teamOneScore + " - " + "[" + GameData.teamTwoScore+ "]";
        p.getTextBounds(currentScore, 0, currentScore.length(), scoreBounds);
    }

    public void Draw(Canvas c){
        c.drawText(currentScore,
                c.getClipBounds().centerX() - scoreBounds.width() / 2,
                c.getClipBounds().centerY() - c.getClipBounds().height()/2 + 140, p);
    }

}
