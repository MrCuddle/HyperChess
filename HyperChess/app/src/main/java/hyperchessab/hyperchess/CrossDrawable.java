package hyperchessab.hyperchess;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Simon on 2015-01-14.
 */
public class CrossDrawable extends Drawable {
    Path path;
    Paint paint;

    public CrossDrawable(int left, int top, int right, int bottom){
        super.setBounds(left,top,right,bottom);
        paint = new Paint();
        paint.setColor(R.color.MainLighter);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);

        Rect bounds = new Rect(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom);

        int margin = 20;
        path = new Path();

        path.moveTo(bounds.left + margin, bounds.top + margin);
        path.lineTo(bounds.right - margin,bounds.bottom - margin);
        path.moveTo(bounds.right - margin, bounds.top + margin);
        path.lineTo(bounds.left + margin, bounds.bottom - margin);
        path.moveTo(bounds.left + margin, bounds.top + margin);
        path.lineTo(bounds.left + margin, bounds.bottom - margin);
        path.lineTo(bounds.right - margin, bounds.bottom - margin);
        path.lineTo(bounds.right - margin, bounds.top + margin);
        path.lineTo(bounds.left + margin, bounds.top + margin);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path,paint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
