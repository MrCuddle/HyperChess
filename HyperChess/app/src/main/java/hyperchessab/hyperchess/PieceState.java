package hyperchessab.hyperchess;

import java.util.List;

/**
 * Created by Perlwin on 12/01/2015.
 */
public class PieceState {

    public int gridPosX;
    public int gridPosY;
    public int startPosX;
    public int startPosY;
    public int shapeType;
    public int owner;
    public boolean hasFlag;
    public boolean selected;
    public List<MovePattern> movePatterns;
    public boolean attackQueued;
    public int attackX, attackY;
    public int attackRange;
    public int HP;
    public int initHP;
    public int respawnTimer;

}
