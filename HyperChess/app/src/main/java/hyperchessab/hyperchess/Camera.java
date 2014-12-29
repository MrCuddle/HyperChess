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

    public Matrix getTransform(){
        transform = new Matrix();

        transform.setScale(scale,scale);
        transform.preTranslate(dx,dy);
        return transform;
    }

    public Camera(){
        dx = 0;
        dy = 0;
        scale = 1;
        transform = new Matrix();
    }

    public void setScale(float scaleFactor){
        scale = scaleFactor;
    }

    public void setTranslation(float x, float y){
        dx = x;
        dy = y;
    }

}
