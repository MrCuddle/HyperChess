package hyperchessab.hyperchess;

/**
 * Created by Perlwin on 12/01/2015.
 */
//Used to send a turn's events to the database
public class Turn {

    int moveStartX, moveStartY, moveEndX, moveEndY, attackX, attackY, player;

    public int getMoveStartX() {
        return moveStartX;
    }

    public void setMoveStartX(int moveStartX) {
        this.moveStartX = moveStartX;
    }

    public int getMoveStartY() {
        return moveStartY;
    }

    public void setMoveStartY(int moveStartY) {
        this.moveStartY = moveStartY;
    }

    public int getMoveEndX() {
        return moveEndX;
    }

    public void setMoveEndX(int moveEndX) {
        this.moveEndX = moveEndX;
    }

    public int getMoveEndY() {
        return moveEndY;
    }

    public void setMoveEndY(int moveEndY) {
        this.moveEndY = moveEndY;
    }

    public int getAttackX() {
        return attackX;
    }

    public void setAttackX(int attackX) {
        this.attackX = attackX;
    }

    public int getAttackY() {
        return attackY;
    }

    public void setAttackY(int attackY) {
        this.attackY = attackY;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public Turn(int moveStartX, int moveStartY, int moveEndX, int moveEndY, int attackX, int attackY, int player){
        this.moveStartX = moveStartX;
        this.moveStartY = moveStartY;
        this.moveEndX = moveEndX;
        this.moveEndY = moveEndY;
        this.attackX = attackX;
        this.attackY = attackY;
        this.player = player;
    }

}
