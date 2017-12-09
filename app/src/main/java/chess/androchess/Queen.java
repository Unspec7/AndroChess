package chess.androchess;

/**
 * Created by Brian on 12/9/2017.
 */

public class Queen extends Piece{
    public Queen (String c, int x, int y) {
        this.xpos = x;
        this.ypos = y;
        this.setColor(c);
        this.type = 'Q';
    }
    public boolean validMove(int oldX, int oldY, int newX, int newY) {
        /**@author Jeff
         * @author Brian
         * Checks if the move is valid for this piece
         */
        deltaX = Math.abs(oldX-newX);
        deltaY = Math.abs(oldY-newY);
        if ( ( deltaX == 0 || deltaY == 0 ) || (deltaX == deltaY) ){

            return true;
        }
        return false;
    }
}
