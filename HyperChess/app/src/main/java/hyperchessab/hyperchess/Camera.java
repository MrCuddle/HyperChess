package hyperchessab.hyperchess;

import android.graphics.Matrix;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class Camera {

    float scale;
    float dx;
    float dy;
    Matrix transform;
    float maxWidth;
    float maxHeight;
    float canvasWidth;
    float canvasHeight;

    public Matrix getTransform(){
        transform = new Matrix();

        transform.setScale(scale,scale);
        transform.preTranslate(dx,dy);

        //Log.d("DX", ""+dx);
        return transform;
    }


    public Camera(){
        dx = 0;
        dy = 0;
        scale = 1;
        transform = new Matrix();
    }

    public void setBounds(float maxWidth, float maxHeight){
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public void setCanvasSize(float canvasWidth, float canvasHeight){
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public void setScale(float scaleFactor){
        scale = scaleFactor;
        LimitBounds();
    }

    public void Translate(float x, float y){
        dx += x;
        dy += y;

        LimitBounds();
    }

    private void LimitBounds(){
        if(dx > 0) dx = 0;
        if(dy > 0) dy = 0;

        if(maxWidth > canvasWidth && -dx*scale + canvasWidth > maxWidth*scale){
            if(maxHeight * scale <= canvasHeight)
                dx = 0;
            else
                dx = canvasWidth/scale - maxWidth;
        }

        if(maxHeight > canvasHeight && -dy*scale + canvasHeight > maxHeight*scale) {
            if(maxHeight * scale <= canvasHeight)
                dy = 0;
            else
                dy = canvasHeight / scale - maxHeight;
        }
    }

}
