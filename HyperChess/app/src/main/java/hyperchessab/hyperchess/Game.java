package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class Game {

    //Bitmap bmp;
    //Drawable icon;

    //float posX = 0f;

    GameBoard board;
    Camera camera;

    public Game(Context context){
        //bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        //icon = context.getResources().getDrawable(R.drawable.ic_launcher);
        //icon.setBounds(0,0,200,200);

        board = new GameBoard(context);
        camera = new Camera();
    }

    public void Update(double dt, float scale, int translationX, int translationY){
        //posX += dt*100.0;
        //icon.setBounds((int)posX,0,(int)posX + 200,200);
        camera.setTranslation(translationX, translationY);
        camera.setScale(scale);
    }

    public void Draw(Canvas c){
        c.setMatrix(camera.getTransform());

        c.drawColor(Color.BLACK);
        //c.drawBitmap(bmp,100,100,null);
        //icon.draw(c);
        board.Draw(c);
    }

}
