package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class Game {

    Bitmap bmp;
    Drawable icon;

    float posX = 0f;

    public Game(Context context){
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        icon = context.getResources().getDrawable(R.drawable.ic_launcher);
        icon.setBounds(0,0,200,200);
    }

    public void Update(double dt){
        posX += dt*100.0;
        icon.setBounds((int)posX,0,(int)posX + 200,200);
    }

    public void Draw(Canvas c){
        c.drawColor(Color.BLUE);
        //c.drawBitmap(bmp,100,100,null);
        icon.draw(c);
    }

}
