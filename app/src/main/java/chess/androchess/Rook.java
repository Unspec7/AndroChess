package chess.androchess;

/**
 * Created by Brian on 12/9/2017.
 */

public class Rook extends Piece{
    public Rook (String c, int x, int y) {
        this.xpos = x;
        this.ypos = y;
        this.setColor(c);
        this.type = 'R';
    }
    public boolean validMove(int oldX, int oldY, int newX, int newY) {
        /**@author Jeff
         * @author Brian
         * Checks if the move is valid for this piece
         */
        if ((oldX!=newX) && (oldY!=newY)) {
            return false;
        }

        return true;
    }
}
