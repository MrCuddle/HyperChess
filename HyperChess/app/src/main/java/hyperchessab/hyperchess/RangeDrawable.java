package hyperchessab.hyperchess;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Perlwin on 13/01/2015.
 */
public class RangeDrawable extends Drawable {
    Paint paint;
    Path path;
    Path path2;
    Path path3;

    int range;

    RangeDrawable(){
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        paint = new Paint();
        paint.setColor(Color.argb(255, 220, 220, 30));
        //paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);

        Rect bounds = new Rect(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom);
        bounds.left = bounds.right - bounds.width()/8;
        bounds.bottom = bounds.top + bounds.height()/8;

        path = new Path();
        path.moveTo(bounds.left, bounds.bottom);
        path.lineTo(bounds.left, bounds.centerY());
        path.lineTo(bounds.centerX(), bounds.top);
        path.lineTo(bounds.right, bounds.centerY());
        path.lineTo(bounds.right, bounds.bottom);
        path.lineTo(bounds.centerX(), bounds.centerY());
        path.lineTo(bounds.left, bounds.bottom);

        bounds = new Rect(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom);
        bounds.left = getBounds().right - getBounds().width()/8;
        bounds.top = getBounds().top + getBounds().height()/8;
        bounds.bottom = getBounds().top + getBounds().height()/4;


        path2 = new Path();
        path2.moveTo(bounds.left, bounds.bottom);
        path2.lineTo(bounds.left, bounds.centerY());
        path2.lineTo(bounds.centerX(), bounds.top);
        path2.lineTo(bounds.right, bounds.centerY());
        path2.lineTo(bounds.right, bounds.bottom);
        path2.lineTo(bounds.centerX(), bounds.centerY());
        path2.lineTo(bounds.left, bounds.bottom);

        bounds = new Rect(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom);
        bounds.left = getBounds().right - getBounds().width()/8;
        bounds.top = getBounds().top + getBounds().height()/4;
        bounds.bottom = getBounds().top + 3*getBounds().height()/8;


        path3 = new Path();
        path3.moveTo(bounds.left, bounds.bottom);
        path3.lineTo(bounds.left, bounds.centerY());
        path3.lineTo(bounds.centerX(), bounds.top);
        path3.lineTo(bounds.right, bounds.centerY());
        path3.lineTo(bounds.right, bounds.bottom);
        path3.lineTo(bounds.centerX(), bounds.centerY());
        path3.lineTo(bounds.left, bounds.bottom);
    }

    public void SetRange(int range){
        this.range = range;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
        if(range >= 2) canvas.drawPath(path2, paint);
        if(range >= 3) canvas.drawPath(path3, paint);
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
