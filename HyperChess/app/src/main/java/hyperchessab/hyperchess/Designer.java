package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by jespe_000 on 2014-12-30.
 */
public class Designer extends Game {
    DesignerListener listener;

    Drawable[][] tiles;
    int[][] directions;
    static final int ARRAYWIDTH = Settings.designerWidth, ARRAYHEIGHT = Settings.designerHeight;
    static final Point STARTPOINT = new Point(0, ARRAYHEIGHT - 1);
    int tileSize = 200;
    PorterDuffColorFilter highlightFilter = new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC);
    MovePattern pattern = new MovePattern();
    ArrayList<Drawable> arrowList = new ArrayList<Drawable>();
    Drawable arrowPrefab;
    Drawable pieceStart;
    Point lastPlacement = new Point(-1, -1);

    public Designer(Context context, Camera camera){
        super(context, camera);
        this.camera = camera;
        arrowPrefab = context.getResources().getDrawable(R.drawable.tile_arrow);

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        tileSize = screenWidth / ARRAYWIDTH;

        tiles = new Drawable[ARRAYWIDTH][ARRAYHEIGHT];
        directions = new int[ARRAYWIDTH][ARRAYHEIGHT + 1];
        for (int x = 0; x < ARRAYWIDTH; x++) {
            for (int y = 0; y < ARRAYHEIGHT; y++) {
                int posx = tileSize * x; int posy = tileSize * y;
                tiles[x][y] = context.getResources().getDrawable(R.drawable.tile_shape);
                tiles[x][y].setBounds(posx, posy, posx + tileSize, posy + tileSize);
                directions[x][y] = MovePattern.Direction.NODIRECTION;
            }
        }

        pieceStart = context.getResources().getDrawable(R.drawable.piece_shape_1);
        pieceStart.setBounds(STARTPOINT.x * tileSize, STARTPOINT.y*tileSize,
                STARTPOINT.x * tileSize + tileSize,
                STARTPOINT.y * tileSize + tileSize);

        AddToPattern(tiles[STARTPOINT.x][STARTPOINT.y], STARTPOINT.x, STARTPOINT.y);
        HighlightAdjacent(STARTPOINT.x, STARTPOINT.y);
    }

    public void Reset(){

        directions = new int[ARRAYWIDTH][ARRAYHEIGHT + 1];
        pattern.Clear();
        arrowList.clear();
        ClearHighlights();

        AddToPattern(tiles[STARTPOINT.x][STARTPOINT.y], STARTPOINT.x, STARTPOINT.y);
        HighlightAdjacent(STARTPOINT.x, STARTPOINT.y);
    }

    public void SetListener(DesignerListener listener){
        this.listener = listener;
    }


    @Override
    public void Update(double dt) {
        if(InputData.Clicked){
            Point p = InputData.ClickPoint;
            for (int x = 0; x < ARRAYWIDTH; x++) {
                for (int y = 0; y < ARRAYHEIGHT; y++) {
                    if(tiles[x][y].getBounds().contains(p.x, p.y)){
                        if(ValidPlacement(x, y)){
                            AddToPattern(tiles[x][y], x, y);
                            HighlightAdjacent(x, y);
                            break;
                        } else {
                            RemoveFromPattern(x, y);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void Draw(Canvas c) {
        c.setMatrix(camera.getTransform());
        c.drawColor(Color.WHITE);

        for (int x = 0; x < ARRAYWIDTH; x++) {
            for (int y = 0; y < ARRAYHEIGHT; y++) {
                tiles[x][y].draw(c);
            }
        }

        for (int i = 0; i < arrowList.size(); i++) {
            arrowList.get(i).draw(c);
        }

        pieceStart.draw(c);

    }

    private void AddToPattern(Drawable d, int indexX, int indexY){
        int dir = GetDirection(indexX, indexY);
        directions[indexX][indexY] = dir;
        lastPlacement.set(indexX, indexY);

        Drawable clone = (RotateDrawable)arrowPrefab.getConstantState().newDrawable();
        int x, y, w, h;
        x = d.getBounds().centerX();
        y = d.getBounds().centerY();
        w = d.getBounds().right - (tileSize / 4);
        h = d.getBounds().bottom - (tileSize / 4);

        clone.setBounds(x, y, w, h);

        RotateDrawable rotationDrawable = (RotateDrawable)clone;

        switch (dir){
            case MovePattern.Direction.UP:
                rotationDrawable.setLevel(0);
                break;
            case MovePattern.Direction.RIGHT:
                rotationDrawable.setLevel(2500);
                break;
            case MovePattern.Direction.DOWN:
                rotationDrawable.setLevel(5000);
                break;
            case MovePattern.Direction.LEFT:
                rotationDrawable.setLevel(7500);
                break;
        }
        arrowList.add(clone);
        pattern.AddDirection(dir);

        if(listener != null){
            listener.OnDesignerInteraction();
        }
    }

    private void ClearHighlights(){
        for (int x = 0; x < ARRAYWIDTH; x++) {
            for (int y = 0; y < ARRAYHEIGHT; y++) {
                tiles[x][y].clearColorFilter();
            }
        }
    }

    private void HighlightAdjacent(int indexX, int indexY){
        ClearHighlights();
        int left, right, up, down;
        left = indexX - 1; right = indexX + 1; up = indexY - 1; down = indexY + 1;
        if(ValidPlacement(left, indexY)){
            tiles[left][indexY].setColorFilter(highlightFilter);
        }
        if(ValidPlacement(right, indexY)){
            tiles[right][indexY].setColorFilter(highlightFilter);
        }
        if(ValidPlacement(indexX, up)){
            tiles[indexX][up].setColorFilter(highlightFilter);
        }
        if(ValidPlacement(indexX, down)){
            tiles[indexX][down].setColorFilter(highlightFilter);
        }
    }

    private void RemoveFromPattern(int indexX, int indexY){

    }

    private boolean ValidPlacement(int x, int y){
        if(x < 0 || y < 0) { return false; }
        if(x >= ARRAYWIDTH || y >= ARRAYHEIGHT ) { return false; }
        if(lastPlacement.x < 0) { return true; }
        int resX = Math.abs(lastPlacement.x - x);
        int resY = Math.abs(lastPlacement.y - y);
        //Returns true if the space is not already occupied and x, y ar adjacent to the last placement
        return directions[x][y] == MovePattern.Direction.NODIRECTION && resX <= 1 && resY <= 1 && resX != resY;
    }

    private int GetDirection(int indexX, int indexY){
        int res = 0;
        //if last placement.x < 0, this is the first time this method is called
        if(lastPlacement.x < 0){
          return MovePattern.Direction.UP;
        }
        if(indexX == lastPlacement.x){
            res = (indexY < lastPlacement.y) ? MovePattern.Direction.UP : MovePattern.Direction.DOWN;
        } else{
            res = (indexX < lastPlacement.x) ? MovePattern.Direction.LEFT : MovePattern.Direction.RIGHT;
        }
        return res;
    }

    public MovePattern GetPattern(){
        return pattern;
    }

    public void Clear(){
        pattern = new MovePattern();
        arrowList.clear();
    }

    public interface DesignerListener{
        public void OnDesignerInteraction();
    }

}
