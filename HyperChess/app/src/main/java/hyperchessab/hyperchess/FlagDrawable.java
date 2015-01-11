package hyperchessab.hyperchess;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Perlwin on 10/01/2015.
 */
public class FlagDrawable extends Drawable {

    Paint paint;
    Path path;


    FlagDrawable(){
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        paint = new Paint();
        paint.setColor(Color.argb(255,71,107,155));
        //paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);

        Rect bounds = new Rect(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom);

        path = new Path();
        path.moveTo(bounds.left, bounds.top);
        path.lineTo(bounds.right, bounds.top);
        path.lineTo(bounds.centerX() + bounds.width()/4, bounds.centerY() - bounds.width()/4);
        path.lineTo(bounds.right, bounds.centerY());
        path.lineTo(bounds.left + 20, bounds.centerY());
        path.lineTo(bounds.left + 20, bounds.bottom);
        path.lineTo(bounds.left, bounds.bottom);
        path.lineTo(bounds.left, bounds.top);

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
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
