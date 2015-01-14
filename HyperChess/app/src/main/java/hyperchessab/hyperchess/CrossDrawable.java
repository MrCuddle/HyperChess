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

        Rect bounds = new Rect(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom);

        path.moveTo(bounds.left, bounds.top);
        path.lineTo(bounds.right,bounds.bottom);
        path.moveTo(bounds.right, bounds.top);
        path.lineTo(bounds.left,bounds.bottom);
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
