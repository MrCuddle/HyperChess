package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class GamePiece {

    Drawable shape;
    boolean selected = false;
    int gridPosX;
    int gridPosY;
    GameBoard board;
    List<MovePattern> patterns;

    public GamePiece(Context context, int x, int y, GameBoard board){
        gridPosX = x;
        gridPosY = y;
        shape = context.getResources().getDrawable(R.drawable.piece_shape_1);
        shape.setBounds(gridPosX*GameBoard.TileSize, gridPosY*GameBoard.TileSize,
                gridPosX*GameBoard.TileSize + GameBoard.TileSize,
                gridPosY*GameBoard.TileSize + GameBoard.TileSize);
        this.board = board;

        patterns = PatternTest();
    }

    //I högsta grad oviktig test metod.
    private ArrayList<MovePattern> PatternTest(){
        MovePattern pattern1 = new MovePattern();
        pattern1.AddDirection(MovePattern.Direction.UP);
        pattern1.AddDirection(MovePattern.Direction.UP);
        pattern1.AddDirection(MovePattern.Direction.RIGHT);

        MovePattern pattern2 = new MovePattern();
        pattern2.AddDirection(MovePattern.Direction.RIGHT);
        pattern2.AddDirection(MovePattern.Direction.RIGHT);
        pattern2.AddDirection(MovePattern.Direction.DOWN);

        ArrayList<MovePattern> patterns = new ArrayList<MovePattern>();
        patterns.add(pattern1);
        patterns.add(pattern2);

        return patterns;
    }

    public void SetPatterns(List<MovePattern> patterns){
        this.patterns = patterns;
    }

    public void Update(double dt){
        if(InputData.Clicked){
            if(InputData.ClickPoint.x >= shape.getBounds().left && InputData.ClickPoint.x <= shape.getBounds().right && InputData.ClickPoint.y >= shape.getBounds().top && InputData.ClickPoint.y <= shape.getBounds().bottom){
                shape.setColorFilter(Color.WHITE, PorterDuff.Mode.ADD);
                HighlightMoveable();
            } else {
                shape.setColorFilter(Color.BLUE, PorterDuff.Mode.ADD);
            }
        }
    }

    private void HighlightMoveable(){
        for(int i = 0; i < patterns.size(); i++){
            int x = gridPosX;
            int y = gridPosY;
            for(int j = 0; j < patterns.get(i).GetPattern().size(); j++){
                if(patterns.get(i).GetPattern().get(j) == MovePattern.Direction.UP)
                    y--;
                if(patterns.get(i).GetPattern().get(j) == MovePattern.Direction.DOWN)
                    y++;
                if(patterns.get(i).GetPattern().get(j) == MovePattern.Direction.LEFT)
                    x--;
                if(patterns.get(i).GetPattern().get(j) == MovePattern.Direction.RIGHT)
                    x++;
            }
            Tile toHighlight = board.GetTile(x,y);

            if(toHighlight != null)
                toHighlight.Highlight();
        }
    }

    public void Draw(Canvas c){
        shape.draw(c);
    }

    private class MoveDestination{
        int gridPosX, gridPosY;
        MovePattern pathTo;
        Drawable shape;

        public MoveDestination(int x, int y, MovePattern pathTo){
            gridPosX = x;
            gridPosY = y;
            this.pathTo = pathTo;
        }

        public void Draw(Canvas c){
            shape.draw(c);
        }
    }
}
