package hyperchessab.hyperchess;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/**
 * Created by Perlwin on 11/01/2015.
 */
public class HPDrawable extends Drawable {

    protected int HP = 1;
    protected int primaryColor = Color.argb(255,50,50,255);
    protected int secondaryColor = Color.argb(255,100,100,255);
    protected int tertiaryColor = Color.argb(255,150,150,255);

    public void setHP(int i){
        HP = i;
    }

    public void setColor(int primary, int secondary, int tertiary){
        primaryColor = primary;
        secondaryColor = secondary;
        tertiaryColor = tertiary;

        setBounds(getBounds());
    }

    public void Disable(){
        primaryColor = Color.argb(255,117,148,191);
        secondaryColor = Color.argb(255, 97, 128, 171);
        tertiaryColor = Color.argb(255,77,108,151);

        setBounds(getBounds());
    }

    @Override
    public void draw(Canvas canvas) {

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
