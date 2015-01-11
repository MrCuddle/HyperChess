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
public class GamePiece extends GameObject {

    Drawable shape;
    Player owner;
    Flag flag;
    float posX, posY;
    boolean selected;
    int gridPosX;
    int gridPosY;
    int attackRange;
    int HP;
    GameBoard board;
    List<MovePattern> patterns;
    List<MoveDestination> moveDestinations;
    List<AttackDestination> attackDestinations;
    LinkedList<Point> movePath;
    boolean isMoving;
    Context context;
    String name = Settings.defaultPieceName;

    int teamColor;


    public GamePiece(Context context, int x, int y, GameBoard board){
        this.context = context;
        gridPosX = x;
        gridPosY = y;
        posX = gridPosX * GameBoard.TileSize;
        posY = gridPosY * GameBoard.TileSize;
        //shape = context.getResources().getDrawable(R.drawable.piece_shape_1);
        shape = new Piece1Drawable();
        shape.setBounds(gridPosX*GameBoard.TileSize, gridPosY*GameBoard.TileSize,
                gridPosX*GameBoard.TileSize + GameBoard.TileSize,
                gridPosY*GameBoard.TileSize + GameBoard.TileSize);
        this.board = board;

        patterns = PatternTest();
        selected = false;
    }

    public void SetOwner(Player owner){
        this.owner = owner;
        this.teamColor = owner.GetTeamColor();
        //shape.setColorFilter(teamColor, PorterDuff.Mode.SRC);
        ((HPDrawable)shape).setColor(owner.GetPrimaryColor(),owner.GetSecondaryColor(),owner.GetTertiaryColor());
    }

    public Player GetOwner(){
        return owner;
    }

    public void SetAttackRange(int range){
        this.attackRange = range;
    }

    public void SetHP(int hp){
        HP = hp;
        ((HPDrawable)shape).setHP(hp);
    }

    public int GetHP(){
        return HP;
    }
    //I högsta grad oviktig test metod.
    private ArrayList<MovePattern> PatternTest(){
        MovePattern pattern1 = new MovePattern();
        pattern1.AddDirection(MovePattern.Direction.UP);
//        pattern1.AddDirection(MovePattern.Direction.UP);
//        pattern1.AddDirection(MovePattern.Direction.RIGHT);

        MovePattern pattern2 = new MovePattern();
//        pattern2.AddDirection(MovePattern.Direction.RIGHT);
//        pattern2.AddDirection(MovePattern.Direction.RIGHT);
        pattern2.AddDirection(MovePattern.Direction.DOWN);

        MovePattern pattern3 = new MovePattern();
//        pattern3.AddDirection(MovePattern.Direction.DOWN);
//        pattern3.AddDirection(MovePattern.Direction.DOWN);
        pattern3.AddDirection(MovePattern.Direction.LEFT);

        MovePattern pattern4 = new MovePattern();
        pattern4.AddDirection(MovePattern.Direction.RIGHT);
//        pattern4.AddDirection(MovePattern.Direction.LEFT);
//        pattern4.AddDirection(MovePattern.Direction.LEFT);
//        pattern4.AddDirection(MovePattern.Direction.UP);

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
            board.getGame().currentGameState = Game.GameState.Attacking;
            Select();
            HighlightAttackable();
            if(flag == null && board.IsOnFlag(gridPosX, gridPosY)) {
                flag = board.GetFlag();
                flag.SetHolder(this);
            }
            if(flag != null){
                flag.SetGridPosition(gridPosX, gridPosY);
                if(board.GetTile(gridPosX, gridPosY) instanceof GoalTile){
                    flag.ResetPosition();
                    flag = null;
                    GameData.IncrementScore(owner.GetTeamId());
                }
            }
        }
        else{
            if(targetPos.x < posX)
                posX -= 20;
            else if(targetPos.x > posX)
                posX += 20;
            if(targetPos.y < posY)
                posY -= 20;
            else if(targetPos.y > posY)
                posY += 20;
            shape.setBounds((int)posX, (int)posY, (int)posX+GameBoard.TileSize, (int)posY+GameBoard.TileSize);
            if(Math.sqrt(Math.pow(targetPos.x - posX, 2) + Math.pow(targetPos.y - posY, 2)) < 2) {
                posX = targetPos.x;
                posY = targetPos.y;
                movePath.poll();
            }
        }
    }

    public Point GetPosition(){
        return new Point(shape.getBounds().left, shape.getBounds().top);
    }
    public void Update(double dt){
        switch(board.getGame().currentGameState) {
            case Moving:
                Move();
                break;
            case Attacking:
                Attack();
                break;
        }
    }

    private void DropFlag(){
        if(flag != null) {
            flag.OnFlagDropped();
        }
        flag = null;
    }

    private void Move(){
        if (isMoving)
            Animate();
        if(owner != board.getGame().players.get(board.getGame().currentPlayer))
            return;
        if (InputData.Clicked) {
            boolean moved = false;
            if (moveDestinations != null) {
                for (MoveDestination d : moveDestinations) {
                    if (d.ClickedOn()) {
                        //SetPosition(newPos.x, newPos.y);
                        movePath = GetMovePath(d.GetPath());
                        isMoving = true;
                        Point newPos = d.GetPosition();
                        board.GetTile(gridPosX, gridPosY).occupier = null;
                        gridPosX = newPos.x;
                        gridPosY = newPos.y;
                        board.GetTile(gridPosX, gridPosY).occupier = this;
                        moveDestinations = null;
                        shape.setColorFilter(teamColor, PorterDuff.Mode.SRC);
                        moved = true;
                    }
                }
            }

            if (!moved) {
                if (InputData.ClickPoint.x >= shape.getBounds().left && InputData.ClickPoint.x <= shape.getBounds().right && InputData.ClickPoint.y >= shape.getBounds().top && InputData.ClickPoint.y <= shape.getBounds().bottom) {
                    Select();
                    HighlightMoveable();
                } else {
                    Deselect();
                }
            }
        }
    }

    private void Attack(){
        if(selected){
            if(attackDestinations.size() == 0) {
                board.getGame().currentGameState = Game.GameState.Moving;
                board.getGame().IncrementCurrentPlayer();
                Deselect();
            }
            if(InputData.Clicked) {
                int x = (int)(InputData.ClickPoint.x / GameBoard.TileSize);
                if(InputData.ClickPoint.x < 0) x = -1;
                int y = (int)(InputData.ClickPoint.y / GameBoard.TileSize);
                if(InputData.ClickPoint.y < 0) y = -1;
                Tile t = board.GetTile(x,y);
                if(t != null && t.occupier instanceof GamePiece && ((GamePiece) t.occupier).GetOwner() != this.owner){
                    if(Math.abs(gridPosX - x) > 0 && Math.abs(gridPosX - x) <= attackRange && Math.abs(gridPosY - y) == 0
                            || Math.abs(gridPosY - y) > 0 && Math.abs(gridPosY - y) <= attackRange && Math.abs(gridPosX - x) == 0){
                        ((GamePiece) t.occupier).DropFlag();
                        board.pieces.remove(t.occupier);
                        t.occupier = null;
                        attackDestinations = null;
                        board.getGame().currentGameState = Game.GameState.Moving;
                        board.getGame().IncrementCurrentPlayer();
                        Deselect();
                    }
                }
            }
        }
    }

    public void Select(){
        shape.setColorFilter(Color.WHITE, PorterDuff.Mode.ADD);
        selected = true;
        board.Select(this);
    }

    public void Deselect(){
        shape.setColorFilter(teamColor, PorterDuff.Mode.SRC);
        moveDestinations = null;
        selected = false;
    }

    private void HighlightAttackable(){
        attackDestinations = new ArrayList<AttackDestination>();
        for(int i = -attackRange; i <= attackRange; i++){
            Tile t = board.GetTile(gridPosX + i, gridPosY);
            if(t != null && t.occupier instanceof GamePiece && (((GamePiece) t.occupier).GetOwner() != this.owner))
                attackDestinations.add(new AttackDestination(gridPosX+i, gridPosY));
            t = board.GetTile(gridPosX, gridPosY + i);
            if(t != null && t.occupier instanceof GamePiece && (((GamePiece) t.occupier).GetOwner() != this.owner))
                attackDestinations.add(new AttackDestination(gridPosX, gridPosY + i));
        }
    }

    private void HighlightMoveable(){

        moveDestinations = new ArrayList<MoveDestination>();
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

                if(board.GetTile(x,y) == null || board.GetTile(x,y).occupier != null){
                    destination = null;
                    break;
                }
            }
            if(destination != null){
                destination.SetPosition(x,y);
                destination.SetPathTo(patterns.get(i));
                moveDestinations.add(destination);
            }
        }
    }

    public void Draw(Canvas c){
        shape.draw(c);
        if(moveDestinations != null) {
            for (MoveDestination d : moveDestinations)
                d.Draw(c);
        }
        if(attackDestinations != null){
            for(AttackDestination d : attackDestinations)
                d.Draw(c);
        }
    }

    private class AttackDestination{
        Drawable shape;
        public AttackDestination(int gridX, int gridY){
            shape = context.getResources().getDrawable(R.drawable.highlight_shape);
            shape.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC);
            shape.setBounds(gridX * GameBoard.TileSize, gridY * GameBoard.TileSize,
                    gridX * GameBoard.TileSize + GameBoard.TileSize, gridY * GameBoard.TileSize + GameBoard.TileSize);

        }


        public void Draw(Canvas c){
            shape.draw(c);
        }
    }
    private class MoveDestination{
        int gridPosX, gridPosY;
        MovePattern pathTo;
        Drawable shape;

        public MoveDestination(){
            shape = context.getResources().getDrawable(R.drawable.highlight_shape);
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