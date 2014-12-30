package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 * Created by jespe_000 on 2014-12-30.
 */
public class Designer extends Game {

    Drawable[][] tiles;
    final int width = 3, height = 3;
    private static final int tileSize = 10;
    Drawable test;
    public Designer(Context context, Camera camera){
        super(context, camera);
        this.camera = camera;
        board = new GameBoard(context, 3, 3);
        tiles = new Drawable[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = context.getResources().getDrawable(R.drawable.tile_shape);
                tiles[x][y].setBounds(x * tileSize,y * tileSize,x + tileSize, y + tileSize);
            }
        }
        test = context.getResources().getDrawable(R.drawable.tile_shape);
        test.setBounds(0, 0, 50, 50);
    }

    @Override
    public void Update(double dt) {
        super.Update(dt);
        if(InputData.Clicked){
            Point p = InputData.ClickPoint;
            int i = p.x;
        }
    }

    @Override
    public void Draw(Canvas c) {
        c.setMatrix(camera.getTransform());
        c.drawColor(Color.WHITE);
        //board.Draw(c);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].draw(c);
            }
        }
        test.draw(c);
    }
}
