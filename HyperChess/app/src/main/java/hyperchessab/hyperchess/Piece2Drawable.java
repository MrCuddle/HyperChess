package hyperchessab.hyperchess;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Perlwin on 11/01/2015.
 */
public class Piece2Drawable extends HPDrawable {

    Paint paint;
    Paint paint2;
    Paint paint3;
    Path path;
    Path path2;
    Path path3;

    Piece2Drawable(){
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        paint = new Paint();
        paint.setColor(tertiaryColor);
        //paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);

        paint2 = new Paint();
        paint2.setColor(secondaryColor);
        //paint.setStrokeWidth(10);
        paint2.setStyle(Paint.Style.FILL);

        paint3 = new Paint();
        paint3.setColor(primaryColor);
        //paint.setStrokeWidth(10);
        paint3.setStyle(Paint.Style.FILL);

    }

    @Override
    public void draw(Canvas canvas) {
        if(HP == 3) canvas.drawCircle(getBounds().exactCenterX(),getBounds().exactCenterY(),getBounds().width()/2 - 10, paint);
        if(HP >= 2) canvas.drawCircle(getBounds().exactCenterX(),getBounds().exactCenterY(),getBounds().width()/2 - 30, paint2);
        canvas.drawCircle(getBounds().exactCenterX(),getBounds().exactCenterY(),getBounds().width()/2 - 50, paint3);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        //do nothing
    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
