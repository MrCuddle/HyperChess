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
    RangeDrawable rangeIndicator;

    Player owner;
    Flag flag;
    float posX, posY;
    boolean selected;
    int gridPosX, gridPosY, startPosX, startPosY;

    int attackRange;
    int HP, initHP;
    int shapeType;
    GameBoard board;
    List<MovePattern> patterns;
    List<MoveDestination> moveDestinations;
    List<AttackDestination> attackDestinations;
    LinkedList<Point> movePath;
    boolean isMoving;
    Context context;
    String name = Settings.defaultPieceName;

    int teamColor;
    boolean attackQueued = false;
    int attackX, attackY;




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

    public GamePiece(Context context, GameBoard board, int shapeType){
        this.context = context;
        this.board = board;
        shape = CreateShape(shapeType);
        this.shapeType = shapeType;
        patterns = PatternTest();
        selected = false;
        rangeIndicator = new RangeDrawable();
    }

    public GamePiece(GamePiece piece)
    {
        this.context = piece.context;
        this.board = piece.board;
        shape = CreateShape(piece.shapeType);
        patterns = piece.patterns;
        selected = piece.selected;
        attackRange = piece.attackRange;
        SetInitHP(piece.HP);
        shapeType = piece.shapeType;
        rangeIndicator = new RangeDrawable();
    }

    public GamePiece(Context context, PieceState state, GameBoard board){
        this.context = context;
        gridPosX = state.gridPosX;
        gridPosY = state.gridPosY;
        startPosX = state.startPosX;
        startPosY = state.startPosY;
        posX = gridPosX * GameBoard.TileSize;
        posY = gridPosY * GameBoard.TileSize;
        switch(state.shapeType){
            case 0:
                shape = new Piece1Drawable();
                break;
            case 1:
                shape = new Piece2Drawable();
                break;
            case 2:
                shape = new Piece3Drawable();
                break;
            case 3:
                shape = new Piece4Drawable();
                break;
        }
        shape.setBounds(gridPosX*GameBoard.TileSize, gridPosY*GameBoard.TileSize,
                gridPosX*GameBoard.TileSize + GameBoard.TileSize,
                gridPosY*GameBoard.TileSize + GameBoard.TileSize);
        selected = state.selected;
        if(selected){
            switch(board.getGame().currentGameState){
                case Moving:
                    HighlightMoveable();
                    break;
                case Attacking:
                    HighlightAttackable();
                    break;
            }
        }
        attackX = state.attackX;
        attackY = state.attackY;
        attackQueued = state.attackQueued;
        if(state.hasFlag){
            flag = board.GetFlag();
            flag.SetHolder(this);
        }
        owner = board.getGame().players.get(state.owner);
        patterns = state.movePatterns;
        attackRange = state.attackRange;
        HP = state.HP;
        initHP = state.initHP;
        ((HPDrawable)shape).setHP(HP);
        ((HPDrawable)shape).setColor(owner.GetPrimaryColor(),owner.GetSecondaryColor(), owner.GetTertiaryColor());

        this.board = board;
        rangeIndicator = new RangeDrawable();
        rangeIndicator.SetRange(attackRange);
    }

    public Drawable CreateShape(int type){
        switch(type)
        {
            case 0:
                return new Piece1Drawable();
            case 1:
                return new Piece2Drawable();
            case 2:
                return new Piece3Drawable();
            case 3:
                return new Piece4Drawable();
            default:
                return new Piece1Drawable();
        }
    }

    public PieceState GetPieceState(){
        PieceState ps = new PieceState();
        ps.attackQueued = attackQueued;
        ps.attackX = attackX;
        ps.attackY = attackY;
        ps.gridPosX = gridPosX;
        ps.gridPosY = gridPosY;
        ps.startPosX = startPosX;
        ps.startPosY = startPosY;
        ps.hasFlag = flag != null;
        ps.movePatterns = patterns;
        ps.owner = (board.getGame().players.get(0) == owner ? 0 : 1);
        ps.shapeType = shapeType;
        ps.selected = selected;
        ps.attackRange = attackRange;
        ps.HP = HP;
        ps.initHP = initHP;
        return ps;
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

    public void SetInitHP(int hp){
        initHP = HP = hp;
        ((HPDrawable)shape).setHP(hp);
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
        rangeIndicator.setBounds(shape.getBounds().left, shape.getBounds().top , shape.getBounds().right,shape.getBounds().bottom);
    }

    public void SetStartPosition(int x, int y){
        startPosX = x;
        startPosY = y;
    }

    private void ResetPosition(){
        if(board.tiles[startPosX][startPosY].occupier == null) {
            SetPosition(startPosX, startPosY);
            board.tiles[startPosX][startPosY].occupier = this;
        }
        else{
            ArrayList<Point> startPositions;
            if(board.getGame().players.get(0) == owner) {
                startPositions = board.GetStartPositions(0);
                }
            else{
                startPositions = board.GetStartPositions(1);
            }

            for(Point p : startPositions){
                if(board.tiles[p.x][p.y].occupier == null){
                    SetPosition(p.x,p.y);
                    board.tiles[p.x][p.y].occupier = this;
                    return;
                }
            }
        }
        SetHP(initHP);
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
                if(board.GetTile(gridPosX, gridPosY) instanceof GoalTile && ((GoalTile) board.GetTile(gridPosX, gridPosY)).owner == owner){
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
            rangeIndicator.setBounds(shape.getBounds().left, shape.getBounds().top , shape.getBounds().right,shape.getBounds().bottom);
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

    public void SimulateMove(int endX, int endY, int attackX, int attackY){
        synchronized (board.getGame().sync) {
            InputData.Clicked = true;
            InputData.ClickPoint = new Point((int) ((endX + 0.5) * GameBoard.TileSize), (int) ((endY + 0.5) * GameBoard.TileSize));
            HighlightMoveable();
            for (MoveDestination d : moveDestinations) {
                if (d.ClickedOn()) {

                    movePath = GetMovePath(d.GetPath());
                    isMoving = true;
                    Point newPos = d.GetPosition();
                    board.GetTile(gridPosX, gridPosY).occupier = null;
                    gridPosX = newPos.x;
                    gridPosY = newPos.y;
                    board.GetTile(gridPosX, gridPosY).occupier = this;
                    moveDestinations = null;

                }
            }
            InputData.Clear();
            if (attackX != -1)
                QueueAttack(attackX, attackY);
        }
    }

    public void QueueAttack(int x, int y){
        attackQueued = true;
        attackX = x;
        attackY = y;
    }

    private void Move(){
        if (isMoving)
            Animate();

        if(board.getGame().online && !board.getGame().MyTurn())
            return;

        if(owner != board.getGame().players.get(board.getGame().currentPlayer))
            return;
        if (InputData.Clicked) {
            boolean moved = false;
            if (moveDestinations != null) {
                for (MoveDestination d : moveDestinations) {
                    if (d.ClickedOn()) {

                        movePath = GetMovePath(d.GetPath());
                        isMoving = true;
                        Point newPos = d.GetPosition();

                        //If we're online, record the move
                        if(board.getGame().online) board.getGame().RecordMove(gridPosX, gridPosY, newPos.x, newPos.y);

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
            if(!attackQueued && attackDestinations.size() == 0) {
                board.getGame().currentGameState = Game.GameState.Moving;
                board.getGame().IncrementCurrentPlayer();
                Deselect();
            }
            if(attackQueued){
                InputData.Clicked = true;
                InputData.ClickPoint = new Point((int)((attackX + 0.5) * GameBoard.TileSize), (int)((attackY + 0.5) * GameBoard.TileSize));
                attackQueued = false;
            }

            if(InputData.Clicked) {
                int x = (int)(InputData.ClickPoint.x / GameBoard.TileSize);
                if(InputData.ClickPoint.x < 0) x = -1;
                int y = (int)(InputData.ClickPoint.y / GameBoard.TileSize);
                if(InputData.ClickPoint.y < 0) y = -1;
                Tile t = board.GetTile(x,y);
                if(t != null && t.occupier instanceof GamePiece && ((GamePiece)
                        t.occupier).GetOwner() != this.owner && CheckValidAttackDestination(x,y)){
                        ((GamePiece)t.occupier).Hit();
                        if(((GamePiece) t.occupier).GetHP() <= 0) {
                            ((GamePiece) t.occupier).DropFlag();
                            //board.pieces.remove(t.occupier);
                            GamePiece piece = ((GamePiece) t.occupier);
                            t.occupier = null;
                            piece.ResetPosition();
                        }
                        attackDestinations = null;

                        //if we're online, record the attack
                        if(board.getGame().online) board.getGame().RecordAttack(x,y);

                        board.getGame().currentGameState = Game.GameState.Moving;
                        board.getGame().IncrementCurrentPlayer();
                        Deselect();
                }
            }
        }
    }

    public void Hit(){
        HP--;
        ((HPDrawable)shape).setHP(HP);
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
        boolean keepSearching = true;
        for(int i = 1; i <= attackRange; i++){
            if(!keepSearching)
                break;
            Tile t = board.GetTile(gridPosX + i, gridPosY);
            if(t != null && t.occupier instanceof Obstacle)
                keepSearching = false;
            else if(t != null && t.occupier instanceof GamePiece && (((GamePiece) t.occupier).GetOwner() != this.owner))
                attackDestinations.add(new AttackDestination(gridPosX+i, gridPosY));
        }

        keepSearching = true;
        for(int i = 1; i <= attackRange; i++){
            if(!keepSearching)
                break;
            Tile t = board.GetTile(gridPosX - i, gridPosY);
            if(t != null && t.occupier instanceof Obstacle)
                keepSearching = false;
            else if(t != null && t.occupier instanceof GamePiece && (((GamePiece) t.occupier).GetOwner() != this.owner))
                attackDestinations.add(new AttackDestination(gridPosX-i, gridPosY));
        }

        keepSearching = true;
        for(int i = 1; i <= attackRange; i++){
            if(!keepSearching)
                break;
            Tile t = board.GetTile(gridPosX, gridPosY + i);
            if(t != null && t.occupier instanceof Obstacle)
                keepSearching = false;
            else if(t != null && t.occupier instanceof GamePiece && (((GamePiece) t.occupier).GetOwner() != this.owner))
                attackDestinations.add(new AttackDestination(gridPosX, gridPosY+i));
        }

        keepSearching = true;
        for(int i = 1; i <= attackRange; i++){
            if(!keepSearching)
                break;
            Tile t = board.GetTile(gridPosX, gridPosY - i);
            if(t != null && t.occupier instanceof Obstacle)
                keepSearching = false;
            else if(t != null && t.occupier instanceof GamePiece && (((GamePiece) t.occupier).GetOwner() != this.owner))
                attackDestinations.add(new AttackDestination(gridPosX, gridPosY-i));
        }
    }

    private boolean CheckValidAttackDestination(int x, int y){
        if(attackDestinations != null){
            for(AttackDestination a : attackDestinations){
                if(x == a.attackDestGridPosX && y == a.attackDestGridPosY)
                    return true;
            }
        }
        return false;
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

    public void DrawPiece(Canvas c){
        shape.draw(c);
    }

    public void DrawPieceOverlays(Canvas c){
        if(selected)
            rangeIndicator.draw(c);
        synchronized (board.getGame().sync) {
            if (moveDestinations != null) {
                for (MoveDestination d : moveDestinations)
                    d.Draw(c);
            }
        }
        if(attackDestinations != null){
            for(AttackDestination d : attackDestinations)
                d.Draw(c);
        }
    }

    private class AttackDestination{
        Drawable shape;
        int attackDestGridPosX, attackDestGridPosY;
        public AttackDestination(int gridX, int gridY){
            attackDestGridPosX = gridX;
            attackDestGridPosY = gridY;
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