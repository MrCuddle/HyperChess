package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by jespe_000 on 2014-12-30.
 */
public class DesignerView extends GameView {
    private Context context;

    public DesignerView(Context context) {
        super(context,false, 0, "");
        this.context = context;
        game = new Designer(context, camera);
    }

    public DesignerView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        game = new Designer(context, camera);
    }

    public DesignerView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        this.context = context;
        game = new Designer(context, camera);
    }

    int test = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if(action == MotionEvent.ACTION_DOWN){
            currentX = event.getX();
            currentY = event.getY();
            clicked = true;
            test++;
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        game.Draw(canvas);
    }

    @Override
    public void Update(double dt) {
        super.Update(dt);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        StartGameLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height, desiredHeight;
        desiredHeight = GetDesigner().GetHeight();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (heightMode){
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(desiredHeight, heightSize);
                break;
            default:
                height = desiredHeight;
                break;
        }
        setMeasuredDimension(widthSize, height);
    }

    public Designer GetDesigner(){
        return (Designer)game;
    }

    public void ResetDesigner(){ GetDesigner().ResetCurrentPiece();}
}
