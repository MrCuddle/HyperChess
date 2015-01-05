package hyperchessab.hyperchess;

import android.graphics.Matrix;
import android.graphics.Point;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class Camera {

    float scale;
    Point scaleOrigin;
    float dx;
    float dy;
    Matrix transform;
    float maxWidth;
    float maxHeight;
    float canvasWidth;
    float canvasHeight;

    public Matrix getTransform(){
        transform = new Matrix();


        transform.setScale(scale, scale, scaleOrigin.x, scaleOrigin.y);
        transform.preTranslate(dx, dy);

        //Log.d("DX", ""+dx);
        return transform;
    }


    public Camera(){
        dx = 0;
        dy = 0;
        scale = 1;
        scaleOrigin = new Point(0,0);
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

    public void setScale(float scaleFactor, Point scaleOrigin){
        scale = scaleFactor;
        this.scaleOrigin = scaleOrigin;
        LimitBounds();
    }

    public void Translate(float x, float y){
        dx += x;
        dy += y;

        LimitBounds();
    }

    private void LimitBounds(){
        Matrix s = new Matrix();
        s.setScale(scale, scale, scaleOrigin.x, scaleOrigin.y);

        Matrix si = new Matrix();
        s.invert(si);
        float[] zero = {0,0};
        si.mapPoints(zero);

        float[] dxdy = {dx,dy};
        s.mapPoints(dxdy);

        if(dxdy[0] > 0) dx = zero[0];
        if(dxdy[1] > 0) dy = zero[1];


        if(-dxdy[0] + canvasWidth > maxWidth*scale){
            if(maxWidth * scale <= canvasWidth) {
                dx = zero[0];
            } else {
                float[] test = {canvasWidth - maxWidth * scale, 0};
                si.mapPoints(test);

                dx = test[0];
            }
        }

        if(-dxdy[1] + canvasHeight > maxHeight*scale){
            if(maxHeight * scale <= canvasHeight) {
                dy = zero[1];
            } else {
                float[] test = {0, canvasHeight - maxHeight * scale};
                si.mapPoints(test);

                dy = test[1];
            }
        }

        /*if(maxWidth > canvasWidth && -dx*scale + canvasWidth > maxWidth*scale){
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
        }*/
    }

}
