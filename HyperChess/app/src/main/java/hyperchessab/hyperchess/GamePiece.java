package hyperchessab.hyperchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Perlwin on 29/12/2014.
 */
public class GamePiece {

    Drawable shape;
    boolean selected = false;
    int gridPosX, gridPosY;
    float posX, posY;
    boolean selected;
    int gridPosX;
    int gridPosY;
    GameBoard board;
    List<MovePattern> patterns;
    List<MoveDestination> destinations;
    LinkedList<Point> movePath;
    boolean isMoving;
    Context context;


    public GamePiece(Context context, int x, int y, GameBoard board){
        this.context = context;
        gridPosX = x;
        gridPosY = y;
        posX = gridPosX * GameBoard.TileSize;
        posY = gridPosY * GameBoard.TileSize;
        shape = context.getResources().getDrawable(R.drawable.piece_shape_1);
        shape.setBounds(gridPosX*GameBoard.TileSize, gridPosY*GameBoard.TileSize,
                gridPosX*GameBoard.TileSize + GameBoard.TileSize,
                gridPosY*GameBoard.TileSize + GameBoard.TileSize);
        this.board = board;

        patterns = PatternTest();
        selected = false;
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

        MovePattern pattern3 = new MovePattern();
        pattern3.AddDirection(MovePattern.Direction.DOWN);
        pattern3.AddDirection(MovePattern.Direction.DOWN);
        pattern3.AddDirection(MovePattern.Direction.LEFT);

        MovePattern pattern4 = new MovePattern();
        pattern4.AddDirection(MovePattern.Direction.LEFT);
        pattern4.AddDirection(MovePattern.Direction.LEFT);
        pattern4.AddDirection(MovePattern.Direction.UP);

        ArrayList<MovePattern> patterns = new ArrayList<MovePattern>();
        patterns.add(pattern1);
        patterns.add(pattern2);
        patterns.add(pattern3);
        patterns.add(pattern4);

        return patterns;
    }

    public void SetPosition(int x, int y){
        gridPosX = x;
        gridPosY = y;
        posX = gridPosX * GameBoard.TileSize;
        posY = gridPosY * GameBoard.TileSize;
        shape.setBounds(gridPosX*GameBoard.TileSize, gridPosY*GameBoard.TileSize,
                gridPosX*GameBoard.TileSize + GameBoard.TileSize,
                gridPosY*GameBoard.TileSize + GameBoard.TileSize);
    }

    public void SetPatterns(List<MovePattern> patterns){
        this.patterns = patterns;
    }

    private LinkedList<Point> GetMovePath(MovePattern pattern){
        LinkedList<Point> path = new LinkedList<Point>();
        int currentX = gridPosX * GameBoard.TileSize;
        int currentY = gridPosY * GameBoard.TileSize;
        for(int move : pattern.GetPattern()){
            if(move == MovePattern.Direction.UP)
                currentY -= GameBoard.TileSize;
            else if(move == MovePattern.Direction.DOWN)
                currentY += GameBoard.TileSize;
            else if(move == MovePattern.Direction.LEFT)
                currentX -= GameBoard.TileSize;
            else if(move == MovePattern.Direction.RIGHT)
                currentX += GameBoard.TileSize;
            path.offer(new Point(currentX, currentY));
        }
        return path;
    }

    private void Animate(){
        Point targetPos = movePath.peek();
        if(targetPos == null) {
            isMoving = false;
            movePath = null;
        }
        else{
            if(targetPos.x < posX)
                posX -= 4;
            else if(targetPos.x > posX)
                posX += 4;
            if(targetPos.y < posY)
                posY -= 4;
            else if(targetPos.y > posY)
                posY += 4;
            shape.setBounds((int)posX, (int)posY, (int)posX+GameBoard.TileSize, (int)posY+GameBoard.TileSize);
            if(Math.sqrt(Math.pow(targetPos.x - posX, 2) + Math.pow(targetPos.y - posY, 2)) < 2) {
                posX = targetPos.x;
                posY = targetPos.y;
                movePath.poll();
            }
        }
    }
    public void Update(double dt){
        if(isMoving)
            Animate();
        if(InputData.Clicked){
            boolean moved = false;
            if(destinations != null){
                for(MoveDestination d : destinations){
                    if(d.ClickedOn()){
                        //SetPosition(newPos.x, newPos.y);
                        movePath = GetMovePath(d.GetPath());
                        isMoving = true;
                        Point newPos = d.GetPosition();
                        gridPosX = newPos.x;
                        gridPosY = newPos.y;
                        destinations = null;
                        shape.setColorFilter(Color.BLUE, PorterDuff.Mode.ADD);
                        moved = true;
                    }
                }
            }

            if(!moved) {
                if (InputData.ClickPoint.x >= shape.getBounds().left && InputData.ClickPoint.x <= shape.getBounds().right && InputData.ClickPoint.y >= shape.getBounds().top && InputData.ClickPoint.y <= shape.getBounds().bottom) {
                    shape.setColorFilter(Color.WHITE, PorterDuff.Mode.ADD);
                    HighlightMoveable();
                    selected = true;
                } else {
                    shape.setColorFilter(Color.BLUE, PorterDuff.Mode.ADD);
                    destinations = null;
                    selected = false;
                }
            }
        }
    }

    private void HighlightMoveable(){

        destinations = new ArrayList<MoveDestination>();
        for(int i = 0; i < patterns.size(); i++){
            int x = gridPosX;
            int y = gridPosY;
            MoveDestination destination = new MoveDestination();
            //Om en destination här inne inte går att nå så sätt referensen över till null
            for(int j = 0; j < patterns.get(i).GetPattern().size(); j++){
                if(patterns.get(i).GetPattern().get(j) == MovePattern.Direction.UP)
                    y--;
                if(patterns.get(i).GetPattern().get(j) == MovePattern.Direction.DOWN)
                    y++;
                if(patterns.get(i).GetPattern().get(j) == MovePattern.Direction.LEFT)
                    x--;
                if(patterns.get(i).GetPattern().get(j) == MovePattern.Direction.RIGHT)
                    x++;

                if(board.GetTile(x,y) == null || board.GetTile(x,y).occupied){
                    destination = null;
                    break;
                }
            }
            if(destination != null){
                destination.SetPosition(x,y);
                destination.SetPathTo(patterns.get(i));
                destinations.add(destination);
            }
        }
    }

    public void Draw(Canvas c){
        shape.draw(c);
        if(destinations != null) {
            for (MoveDestination d : destinations)
                d.Draw(c);
        }
    }

    private class MoveDestination{
        int gridPosX, gridPosY;
        MovePattern pathTo;
        Drawable shape;

        public MoveDestination(){
            shape = context.getResources().getDrawable(R.drawable.piece_shape_1);
            shape.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC);

        }

        public MoveDestination(int x, int y, MovePattern pathTo){
            shape = context.getResources().getDrawable(R.drawable.piece_shape_1);
            shape.setColorFilter(Color.YELLOW, PorterDuff.Mode.ADD);
            SetPosition(x, y);
            this.pathTo = pathTo;
        }

        public MovePattern GetPath(){
            return pathTo;
        }

        public Point GetPosition(){
            return new Point(gridPosX,gridPosY);
        }

        public void SetPosition(int x, int y){
            gridPosX = x;
            gridPosY = y;
            shape.setBounds(gridPosX*GameBoard.TileSize, gridPosY*GameBoard.TileSize,
                    gridPosX*GameBoard.TileSize + GameBoard.TileSize,
                    gridPosY*GameBoard.TileSize + GameBoard.TileSize);
        }

        public void SetPathTo(MovePattern pattern){
            pathTo = pattern;
        }

        public boolean ClickedOn(){
            return InputData.ClickPoint.x >= shape.getBounds().left && InputData.ClickPoint.x <= shape.getBounds().right &&
                   InputData.ClickPoint.y >= shape.getBounds().top && InputData.ClickPoint.y <= shape.getBounds().bottom;
        }


        public void Draw(Canvas c){
            shape.draw(c);
        }
    }
}
