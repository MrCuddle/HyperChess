package hyperchessab.hyperchess;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

/**
 * Created by Perlwin on 10/01/2015.
 */
public class Piece3Drawable extends HPDrawable {

    Paint paint;
    Paint paint2;
    Paint paint3;
    Path path;
    Path path2;
    Path path3;

    Piece3Drawable(){


    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        paint = new Paint();
        paint.setColor(tertiaryColor);
        //paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);

        Rect bounds = new Rect(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom);
        bounds.set(bounds.left + 10, bounds.top + 10, bounds.right - 10, bounds.bottom - 10);

        path = new Path();
        path.moveTo(bounds.exactCenterX(), bounds.top);
        path.lineTo(bounds.right,bounds.top);
        path.lineTo(bounds.right,bounds.bottom);
        path.lineTo(bounds.left,bounds.bottom);
        path.lineTo(bounds.left,bounds.top);
        path.lineTo(bounds.exactCenterX(), bounds.top);

        float radius = 30.0f;

        CornerPathEffect cornerPathEffect =
                new CornerPathEffect(radius);

        paint.setPathEffect(cornerPathEffect);

        paint2 = new Paint();
        paint2.setColor(secondaryColor);
        //paint.setStrokeWidth(10);
        paint2.setStyle(Paint.Style.FILL);

        bounds.set(bounds.left + 20, bounds.top + 20, bounds.right - 20, bounds.bottom - 20);

        path2 = new Path();
        path2.moveTo(bounds.exactCenterX(), bounds.top);
        path2.lineTo(bounds.right,bounds.top);
        path2.lineTo(bounds.right,bounds.bottom);
        path2.lineTo(bounds.left,bounds.bottom);
        path2.lineTo(bounds.left,bounds.top);
        path2.lineTo(bounds.exactCenterX(), bounds.top);

        radius = 25.0f;

        cornerPathEffect =
                new CornerPathEffect(radius);

        paint2.setPathEffect(cornerPathEffect);

        paint3 = new Paint();
        paint3.setColor(primaryColor);
        //paint.setStrokeWidth(10);
        paint3.setStyle(Paint.Style.FILL);

        bounds.set(bounds.left + 20, bounds.top + 20, bounds.right - 20, bounds.bottom - 20);

        path3 = new Path();
        path3.moveTo(bounds.exactCenterX(), bounds.top);
        path3.lineTo(bounds.right,bounds.top);
        path3.lineTo(bounds.right,bounds.bottom);
        path3.lineTo(bounds.left,bounds.bottom);
        path3.lineTo(bounds.left,bounds.top);
        path3.lineTo(bounds.exactCenterX(), bounds.top);

        radius = 20.0f;

        cornerPathEffect =
                new CornerPathEffect(radius);

        paint3.setPathEffect(cornerPathEffect);
    }

    @Override
    public void draw(Canvas canvas) {
        if(HP == 3) canvas.drawPath(path, paint);
        if(HP >= 2) canvas.drawPath(path2, paint2);
        canvas.drawPath(path3, paint3);
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
