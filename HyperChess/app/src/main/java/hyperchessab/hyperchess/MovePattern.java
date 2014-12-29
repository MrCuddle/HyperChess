package hyperchessab.hyperchess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jespe_000 on 2014-12-29.
 */
public class MovePattern {

    public static class Direction{
        public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    }

    private List<Integer> pattern;

    public MovePattern(){ pattern = new ArrayList<Integer>();}
    public MovePattern(List<Integer> pattern) {this.pattern = pattern;}

    public List<Integer> GetPattern() {
        return pattern;
    }

    public void SetPattern(List<Integer> pattern) {
        this.pattern = pattern;
    }

    public void AddDirection(int dir){
        pattern.add(dir);
    }

    //Bör hanteras av GameBoard senare t.ex Highlight(Piece pi, Pattern pa)
    public void HighlightTiles(Tile[][] tiles){
        if(pattern != null && pattern.size() > 0){
            int arrayWidth = tiles.length;
            int arrayHeight = tiles[0].length;
            int x = 0, y = 0;
            for (int i = 0; i < pattern.size(); i++) {
                int dir = pattern.get(i);
                switch (dir){
                    case Direction.UP:
                        y--;
                        break;
                    case Direction.RIGHT:
                        x++;
                        break;
                    case Direction.DOWN:
                        y++;
                        break;
                    case Direction.LEFT:
                        x--;
                        break;
                }

                if(InBounds(arrayWidth, arrayHeight, x, y)){
                    tiles[x][y].Highlight();
                };
            }
        }
    }

    private boolean InBounds(int arrayWidth, int arrayHeight, int x, int y) {
        return x >= 0 && y >= 0 && x < arrayWidth && y < arrayHeight;
    }
}
