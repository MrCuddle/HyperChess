package hyperchessab.hyperchess;

import android.graphics.Canvas;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class GameLoop extends Thread{

    boolean stop;
    public boolean running;
    GameView view;

    public GameLoop(GameView view){
        this.view = view;
        stop = false;
        running = false;
    }

    public void Stop(){
        stop = true;
    }

    @Override
    public void run() {
        stop = false;
        running = true;

        long ticksPS = 1000 / 60;
        long startTime = System.currentTimeMillis();
        long sleepTime = 0;

        while(!stop){

            double dt = startTime;
            startTime = System.currentTimeMillis();
            dt = (startTime - dt)/1000.0;

            Canvas c = null;

            try {
                view.Update(dt);
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()){
                    view.onDraw(c);
                }
            } finally {
                if(c != null)
                    view.getHolder().unlockCanvasAndPost(c);
            }

            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);

            try {
                if (sleepTime > 0) {
                    sleep(sleepTime);
                } else {
                    sleep(5);
                }
            } catch (Exception e){

            }
        }

        running = false;
    }
}
