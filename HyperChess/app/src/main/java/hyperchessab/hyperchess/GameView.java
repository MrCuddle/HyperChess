package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    GameLoop gameLoop;
    Game game;

    public GameView(Context context){
        super(context);
        game = new Game(context);
        gameLoop = new GameLoop(this);
        getHolder().addCallback(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        game.Draw(canvas);
    }

    public void Update(double dt){
        game.Update(dt);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameLoop.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameLoop.Stop();
        while (retry) {
            try {
                gameLoop.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}