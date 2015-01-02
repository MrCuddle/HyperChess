package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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
    Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    MovePattern pattern = new MovePattern();
    ArrayList<Drawable> highlights = new ArrayList<Drawable>();
    Drawable highlightPrefab;
    Drawable arrow;
    Drawable pieceStart;
    Path drawPath = new Path();
    Point lastPlacement = new Point(-1, -1), lastPosition = new Point(-1, -1);

    public Designer(Context context, Camera camera){
        super(context, camera);
        this.camera = camera;
        arrow = context.getResources().getDrawable(R.drawable.tile_arrow);
        highlightPrefab = context.getResources().getDrawable(R.drawable.highlight_shape);

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

        linePaint.setStrokeWidth(10);
        linePaint.setColor(Color.RED);
        linePaint.setStyle(Paint.Style.STROKE);

        pieceStart = context.getResources().getDrawable(R.drawable.piece_shape_1);
        pieceStart.setBounds(STARTPOINT.x * tileSize, STARTPOINT.y*tileSize,
                STARTPOINT.x * tileSize + tileSize,
                STARTPOINT.y * tileSize + tileSize);

        SetStart(STARTPOINT.x, STARTPOINT.y);
        HighlightAdjacent(STARTPOINT.x, STARTPOINT.y);

    }

    public void Reset(){
        directions = new int[ARRAYWIDTH][ARRAYHEIGHT + 1];
        pattern.Clear();
        ClearHighlights();

        SetStart(STARTPOINT.x, STARTPOINT.y);
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

        for (int i = 0; i < highlights.size(); i++) {
            highlights.get(i).draw(c);
        }

        c.drawPath(drawPath, linePaint);
        if(arrow != null){
            arrow.draw(c);
        }
        pieceStart.draw(c);



    }

    private void SetStart(int indexX, int indexY){
        int x = tiles[indexX][indexY].getBounds().centerX();
        int y = tiles[indexX][indexY].getBounds().centerY();
        lastPosition.set(x, y);
        lastPlacement.set(indexX, indexY);
        int dir = GetDirection(indexX, indexY);
        directions[indexX][indexY] = dir;
        drawPath.setLastPoint(x, y);
    }

    private void AddToPattern(Drawable d, int indexX, int indexY){
        int dir = GetDirection(indexX, indexY);
        directions[indexX][indexY] = dir;

        Drawable clone = (RotateDrawable)arrow.getConstantState().newDrawable();
        int x, y, w, h;
        x = d.getBounds().centerX();
        y = d.getBounds().centerY();
        w = d.getBounds().right - (tileSize / 4);
        h = d.getBounds().bottom - (tileSize / 4);

        drawPath.lineTo(x, y);

        clone.setBounds(x, y, w, h);
        lastPosition.set(x, y);
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
        //arrowList.add(clone);
        arrow = clone;

        pattern.AddDirection(dir);
        lastPlacement.set(indexX, indexY);

        if(listener != null){
            listener.OnDesignerInteraction();
        }
    }

    private void ClearHighlights(){
        highlights.clear();
    }

    private void HighlightAdjacent(int indexX, int indexY){
        ClearHighlights();
        int left, right, up, down;
        left = indexX - 1; right = indexX + 1; up = indexY - 1; down = indexY + 1;
        if(ValidPlacement(left, indexY)){
            Drawable d = highlightPrefab.getConstantState().newDrawable();
            d.setBounds(tiles[left][indexY].getBounds());
            highlights.add(d);
        }
        if(ValidPlacement(right, indexY)){
            Drawable d = highlightPrefab.getConstantState().newDrawable();
            d.setBounds(tiles[right][indexY].getBounds());
            highlights.add(d);
        }
        if(ValidPlacement(indexX, up)){
            Drawable d = highlightPrefab.getConstantState().newDrawable();
            d.setBounds(tiles[indexX][up].getBounds());
            highlights.add(d);
        }
        if(ValidPlacement(indexX, down)){
            Drawable d = highlightPrefab.getConstantState().newDrawable();
            d.setBounds(tiles[indexX][down].getBounds());
            highlights.add(d);
        }
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
    }

    public interface DesignerListener{
        public void OnDesignerInteraction();
    }

}
