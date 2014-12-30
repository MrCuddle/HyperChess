package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jespe_000 on 2014-12-30.
 */
public class Designer extends Game {

    Drawable[][] tiles;
    final int width = 3, height = 3;
    static final int tileSize = 200;
    MovePattern pattern = new MovePattern();

    public Designer(Context context, Camera camera){
        super(context, camera);
        this.camera = camera;

        tiles = new Drawable[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int posx = tileSize * x; int posy = tileSize * y;
                tiles[x][y] = context.getResources().getDrawable(R.drawable.tile_shape);
                tiles[x][y].setBounds(posx, posy, posx + tileSize, posy + tileSize);
            }
        }
    }


    @Override
    public void Update(double dt) {
        if(InputData.Clicked){
            Point p = InputData.ClickPoint;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if(Contains(tiles[x][y], p)){
                        AddToPattern(tiles[x][y]);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void Draw(Canvas c) {
        c.setMatrix(camera.getTransform());
        c.drawColor(Color.WHITE);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].draw(c);
            }
        }


    }

    private void AddToPattern(Drawable d){

    }

    private boolean Contains(Drawable d, Point p){
        return p.x > d.getBounds().left && p.x < d.getBounds().right
                && p.y >= d.getBounds().top && p.y <= d.getBounds().bottom;
    }
}
