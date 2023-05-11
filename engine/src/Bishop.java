import java.util.LinkedList;

public class Bishop  extends Piece{
    protected Bishop(boolean isWhite, int pos) {
        super(isWhite, pos);
    }

    public static boolean isMoveValid(int pos, int target, boolean isWhite) {
        if(whitePiece(Main.sBoard.charAt(target)) != isWhite && Main.sBoard.charAt(target) != '0'){
            return false;
        }
        int abs = Math.abs((pos % 8) - (target % 8));
        int abs1 = Math.abs((pos >> 3) - (target >> 3));
        if (abs1 != abs) {                                              //Checks if the destination is on a diagonal
            return false;
        }
        int lineMovement = ((target >> 3) - (pos >> 3)) / abs1;         //1 or -1 for left and right movement
        int fileMovement = ((target % 8) - (pos % 8)) / abs;            //1 or -1 for up and down movement
        for (int i = 1; i < abs1; i++) {                                //Loops through all squares in between
            if (board[(pos >> 3) + i * lineMovement][(pos % 8) + i * fileMovement] != null) {   //Checks if they're null
                return false;
            }
        }
        return true;
    }

    @Override
    public int[] getValidMoves(int pos, boolean isWhite) {
        LinkedList<Integer> moves = new LinkedList<>();
        int tmp = 1;

        while((pos >> 3) - tmp >= 0 && pos % 8 - tmp >= 0){
            if(isFreeSquare(pos - 9 * tmp, pos, isWhite)) moves.add(pos - 9 * tmp);
            if(isSquareBlocked(pos - 9 * tmp)) break;
            tmp++;
        }
        tmp = 1;
        while((pos >> 3) - tmp >= 0 && pos % 8 + tmp < 8){
            if(isFreeSquare(pos - 7 * tmp, pos, isWhite)) moves.add(pos - 7 * tmp);
            if(isSquareBlocked(pos - 7 * tmp)) break;
            tmp++;
        }
        tmp = 1;
        while((pos >> 3) + tmp < 8 && pos % 8 - tmp >= 0){
            if(isFreeSquare(pos + 7 * tmp, pos, isWhite)) moves.add(pos + 7 * tmp);
            if(isSquareBlocked(pos + 7 * tmp)) break;
            tmp++;
        }
        tmp = 1;
        while((pos >> 3) + tmp < 8 && pos % 8 + tmp < 8){
            if(isFreeSquare(pos + 9 * tmp, pos, isWhite)) moves.add(pos + 9 * tmp);
            if(isSquareBlocked(pos + 9 * tmp)) break;
            tmp++;
        }

        return Piece.toArray(moves);
    }

    @Override
    public String getType() {
        return "Bishop";
    }
}