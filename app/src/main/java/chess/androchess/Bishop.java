package chess.androchess;

/**
 * Created by Brian on 12/9/2017.
 */

public class Bishop extends Piece{
    public Bishop (String c, int x, int y) {
        this.xpos = x;
        this.ypos = y;
        this.setColor(c);
        this.type = 'B';
    }
    /**@author Jeff
     * @author Brian
     * Checks if the move is valid for this piece
     */
    public boolean validMove(int oldX, int oldY, int newX, int newY) {
        deltaX = Math.abs(oldX-newX);
        deltaY = Math.abs(oldY-newY);
        if (deltaX == deltaY){

            return true;
        }
        return false;
    }
}
