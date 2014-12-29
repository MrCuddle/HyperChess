package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    GameLoop gameLoop;
    Game game;
    Camera camera;
    float currentX, currentY;
    int mCurrentIndex, mNumOfPointers;
    float scaleFactor = 1.f;

    ScaleGestureDetector scaleGestureDetector;

    public GameView(Context context){
        super(context);
        camera = new Camera();
        game = new Game(context, camera);
        gameLoop = new GameLoop(this);
        getHolder().addCallback(this);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());


    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector){
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.f));
            camera.setScale(scaleFactor);
            invalidate();
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex();

        scaleGestureDetector.onTouchEvent(event);

        mCurrentIndex = index;

        mNumOfPointers = event.getPointerCount();
        if(event.getPointerCount() < 2) {
            if(action == MotionEvent.ACTION_DOWN){
                currentX = event.getX();
                currentY = event.getY();
            }
            float dx = event.getX() - currentX;
            float dy = event.getY() - currentY;

            dx /= scaleFactor;
            dy /= scaleFactor;
            camera.Translate(dx,dy);

            currentX = event.getX();
            currentY = event.getY();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        game.Draw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText("currentX: " + currentX, 0,40, paint);
        canvas.drawText("currentY: " + currentY, 0,80, paint);
        canvas.drawText("currentActionIndex: " + mCurrentIndex, 0,120, paint);
        canvas.drawText("numOfPointers: " + mNumOfPointers, 0,160, paint);
        canvas.drawText("scaleFactor: " + scaleFactor, 0, 200, paint);
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