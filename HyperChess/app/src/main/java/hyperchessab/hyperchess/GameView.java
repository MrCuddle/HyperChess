package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.AttributeSet;
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
    float currentX, currentY, startTouchX, startTouchY;
    float scaleFactor = 1.0f;
    boolean clicked = false;
    boolean stoppedZooming = false;
    float canvasWidth = 100.0f;
    Point scaleOrigin;

    ScaleGestureDetector scaleGestureDetector;

    public GameView(Context context){
        super(context);
        Init(context);
    }

    public GameView(Context context, AttributeSet attr){
        super(context, attr);
        Init(context);
    }

    public GameView(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
        Init(context);
    }

    private void Init(Context context){
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
            scaleOrigin = new Point((int)detector.getFocusX(),(int)detector.getFocusY());

            //Calculate appropriate max/min zoom based on the canvas size:
            float boardWidth = game.board.Width * GameBoard.TileSize;
            float minScale = canvasWidth/boardWidth;
            float maxScale = minScale * 4.0f;

            scaleFactor = Math.max(minScale, Math.min(scaleFactor, maxScale));

            camera.setScale(scaleFactor, scaleOrigin);
            invalidate();
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        scaleGestureDetector.onTouchEvent(event);

        //Check if the user has stopped pinching - if so, make a note so that the start position for panning can be reset
        if(event.getPointerCount() == 2) {
            if(action == MotionEvent.ACTION_POINTER_UP) {
                stoppedZooming = true;
                return true;
            }
        }

        //Panning
        if(event.getPointerCount() < 2) {

            if(action == MotionEvent.ACTION_DOWN){
                startTouchX = currentX = event.getX();
                startTouchY = currentY = event.getY();

            }
            //Reset the current position if the user just stopped pinching
            if(stoppedZooming){
                currentX = event.getX();
                currentY = event.getY();
                stoppedZooming = false;
            }
            float dx = event.getX() - currentX;
            float dy = event.getY() - currentY;


            float offsetX = event.getX() - startTouchX;
            float offsetY = event.getY() - startTouchY;
            if(Math.sqrt(offsetX*offsetX +offsetY*offsetY) < 60){
                if(action == MotionEvent.ACTION_UP)
                    clicked = true;
            }
            else {
                dx /= scaleFactor;
                dy /= scaleFactor;
                camera.Translate(dx, dy);
            }
            currentX = event.getX();
            currentY = event.getY();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvasWidth = canvas.getWidth();
        camera.setCanvasSize(canvas.getWidth(),canvas.getHeight());
        game.Draw(canvas);
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        canvas.drawText("currentX: " + currentX, 0,40, paint);
//        canvas.drawText("currentY: " + currentY, 0,80, paint);
//        canvas.drawText("scaleFactor: " + scaleFactor, 0, 120, paint);
//        canvas.drawText("Clicked: " + (clicked ? "yupp" : "nope"), 0, 160, paint);
    }

    public void Update(double dt){
        if(clicked){
            Matrix inverse = new Matrix();
            camera.getTransform().invert(inverse);
            float[] points = {currentX, currentY};
            inverse.mapPoints(points);
            InputData.ClickPoint = new Point((int)points[0], (int)points[1]);
            InputData.Clicked = true;
        }
        game.Update(dt);
        InputData.Clear();
        clicked = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        StartGameLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        StopGameLoop();
    }

    public void StartGameLoop(){
        if(gameLoop.running)
            return;
        gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    public void StopGameLoop(){
        if(!gameLoop.running)
            return;
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