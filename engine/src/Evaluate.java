public class Evaluate {

/*
* This class returns an evaluation for any given position.
* The evaluation is returned as a double and indicates the advantage
* of either side.
* The evaluation is judged by 15 factors like pawn-structure, material,
* controlled area, king safety and more.
* During the initiation of an Object, each factor gets a multiplier set
* between 0 and 1. This multiplier sets the significance of the factor
* for the evaluation.
*/

    char[][] board = new char[8][8];    // the board is set as a 2d char-array
    double E = 0.0;                     // the starting evaluation value for any position is 0
    boolean endgame = false;            // indicates the current game-phase

    int value_White;
    int value_Black;
    int whiteActivity;
    int blackActivity;
    int whiteArea;
    int blackArea;

    int value_King = 1000;
    int value_Queen = 10;
    int value_Rook = 5;
    int value_minorPiece = 3;
    int value_Pawn = 1;

    double value_passedPawn;
    double value_isolatedPawn;
    double value_stackedPawn;
    double value_centralPawn ;
    double value_advancedPawn;
    double value_centralKnight;
    double value_centralRook;
    double value_seventhRankRook;
    double value_pawnChain;
    double value_activity;
    double value_area;
    double value_kingSafety;
    double value_kingLead;
    double value_kingOpposition;

    public Evaluate(
            double value_passedPawn,
            double value_isolatedPawn,
            double value_stackedPawn,
            double value_centralPawn,
            double value_advancedPawn,
            double value_centralKnight,
            double value_centralRook,
            double value_seventhRankRook,
            double value_pawnChain,
            double value_activity,
            double value_area,
            double value_kingSafety,
            double value_kingLead,
            double value_kingOpposition
    )
    {
        this.value_passedPawn = value_passedPawn;
        this.value_isolatedPawn = value_isolatedPawn;
        this.value_stackedPawn = value_stackedPawn;
        this.value_centralPawn = value_centralPawn;
        this.value_advancedPawn = value_advancedPawn;
        this.value_centralKnight = value_centralKnight;
        this.value_centralRook = value_centralRook;
        this.value_seventhRankRook = value_seventhRankRook;
        this.value_pawnChain = value_pawnChain;
        this.value_activity = value_activity;
        this.value_area = value_area;
        this.value_kingSafety = value_kingSafety;
        this.value_kingLead = value_kingLead;
        this.value_kingOpposition = value_kingOpposition;
    }

    public Evaluate(double[] values){
        this.value_passedPawn = values[0];
        this.value_isolatedPawn = values[1];
        this.value_stackedPawn = values[2];
        this.value_centralPawn = values[3];
        this.value_advancedPawn = values[4];
        this.value_centralKnight = values[5];
        this.value_centralRook = values[6];
        this.value_seventhRankRook = values[7];
        this.value_pawnChain = values[8];
        this.value_activity = values[9];
        this.value_area = values[10];
        this.value_kingSafety = values[11];
        this.value_kingLead = values[12];
        this.value_kingOpposition = values[13];
    }


    public double evaluate(char[][] array, int[][] whiteMoves, int[][] blackMoves){
        this.board = array;

        // resets the evaluation values
        value_White = 0;
        value_Black = 0;
        whiteActivity = 0;
        blackActivity = 0;
        whiteArea = 0;
        blackArea = 0;
        E = 0;

        // The following for-loops count the material on the board

        for (int y = 0; y<8; y++) {
            for (int x = 0; x<8; x++) {
                if (Character.toLowerCase(board[y][x]) == 'p'){
                    if (Character.toLowerCase(board[y][x]) == board[y][x]){
                        value_Black += value_Pawn;
                    } else {
                        value_White += value_Pawn;
                    }
                }
                if (Character.toLowerCase(board[y][x]) == 'n' || Character.toLowerCase(board[y][x]) == 'b'){
                    if (Character.toLowerCase(board[y][x]) == board[y][x]){
                        value_Black += value_minorPiece;
                    } else {
                        value_White += value_minorPiece;
                    }
                }
                if (Character.toLowerCase(board[y][x]) == 'r'){
                    if (Character.toLowerCase(board[y][x]) == board[y][x]){
                        value_Black += value_Rook;
                    } else {
                        value_White += value_Rook;
                    }
                }
                if (Character.toLowerCase(board[y][x]) == 'q'){
                    if (Character.toLowerCase(board[y][x]) == board[y][x]){
                        value_Black += value_Queen;
                    } else {
                        value_White += value_Queen;
                    }
                }
                // the king safety gets evaluated by looking at the surrounding squares
                if (Character.toLowerCase(board[y][x]) == 'k'){
                    if (Character.toLowerCase(board[y][x]) == board[y][x]){
                        value_Black += value_King;
                        if (y != 0 && board[y-1][x] == 'p'){
                            E -= (1/3.0) * value_kingSafety;
                        }
                        if (y != 0 && x != 0 && board[y-1][x-1] == 'p'){
                            E -= (1/3.0) * value_kingSafety;
                        }
                        if (y != 0 && x != 7 && board[y-1][x+1] == 'p'){
                            E -= (1/3.0) * value_kingSafety;
                        }
                    } else {
                        value_White += value_King;
                        if (y != 7 && board[y+1][x] == 'P'){
                            E += (1/3.0) * value_kingSafety;
                        }
                        if (y != 7 && x != 0 && board[y+1][x-1] == 'P'){
                            E += (1/3.0) * value_kingSafety;
                        }
                        if (y != 7 && x != 7 && board[y+1][x+1] == 'P'){
                            E += (1/3.0) * value_kingSafety;
                        }
                    }
                }

            }
        }

        E += value_White;
        E -= value_Black;

        // the endgame-phase gets initiated, if less the 16 points
        // of material per side are left on the board (not counting the king)

        if ((value_White + value_Black)/2.0 <= 1016) {
            endgame = true;
        }

        String wm = new String("");
        String subwm = new String("");
        String bm = new String("");
        String subbm = new String("");

        // controlled are and piece activity gets measured and
        // added to the evaluation

        whiteActivity = whiteMoves.length;
        blackActivity = blackMoves.length;
        for (int w = 0; w<whiteMoves.length; w++){
            subwm = "<" + whiteMoves[w][2] + "" + whiteMoves[w][2] + ">";
            if (!wm.contains(subwm)){
                wm += subwm;
                whiteArea++;
            }
        }
        for (int b = 0; b<blackMoves.length; b++){
            subbm = "<" + blackMoves[b][2] + "" + blackMoves[b][2] + ">";
            if (!bm.contains(subbm)){
                bm += subbm;
                blackArea++;
            }
        }

        E += whiteActivity * value_activity;
        E -= blackActivity * value_activity;
        E += whiteArea * value_area;
        E -= blackArea * value_area;

        for (int r = 0; r<8; r++){
            for (int f = 0; f<8; f++){

                check(board, r, f);

            }
        }
        return E;
    }



    public String[] getSurroundings(int r, int f, boolean squares){
    /*
    * This class returns the surrounding squares or pieces of any given square
    */

        String[] result = new String[8];

        for (int i = 0; i<8; i++) {
            result[i] = "/";
        }

        if (r != 0 && f != 0){
            if (squares) {
                result[0] = ""+board[r-1][f-1];
            } else {
                result[0] = (r-1) + "" + (f-1);
            }
        }
        if (r != 0) {
            if (squares) {
                result[1] = "" + board[r - 1][f];
            } else {
                result[1] = (r - 1) + "" + f;
            }
        }
        if (r != 0 && f != 7) {
            if (squares) {
                result[2] = "" + board[r - 1][f+1];
            } else {
                result[2] = (r - 1) + "" + (f+1);
            }
        }
        if (f != 0) {
            if (squares) {
                result[3] = "" + board[r][f-1];
            } else {
                result[3] = (r) + "" + (f-1);
            }
        }
        if (f != 7) {
            if (squares) {
                result[4] = "" + board[r][f+1];
            } else {
                result[4] = (r) + "" + (f+1);
            }
        }
        if (r != 7 && f != 0){
            if (squares) {
                result[5] = ""+board[r+1][f-1];
            } else {
                result[5] = (r+1) + "" + (f-1);
            }
        }
        if (r != 7) {
            if (squares) {
                result[6] = "" + board[r + 1][f];
            } else {
                result[6] = (r + 1) + "" + f;
            }
        }
        if (r != 7 && f != 7) {
            if (squares) {
                result[7] = "" + board[r + 1][f+1];
            } else {
                result[7] = (r + 1) + "" + (f+1);
            }
        }

        return result;

    }


    public int checkFile(int file, char character){
        /*
        * returns the number of specified pieces in a certain file
        */

        int counter = 0;

        for (int r = 0; r<8; r++){
            if (board[r][file] == character){
                counter++;
            }
        }
        return counter;
    }



    public void pawn(int r, int f) {
        double n = 0;
        char current;
        char opponent;
        int m = 1;                  // this multiplier is positive for white and negative for black

        if (board[r][f] == 'P') {   // pawn is white
            current = 'P';
            opponent = 'p';
        } else {                    // pawn is black
            current = 'p';
            opponent = 'P';
            m = -1;
        }


        // this code checks if the given pawn is passed, meaning if it has
        // passed all pawns that could stop it
        if (checkFile(f, opponent) == 0) {          // checks if the file is free of opposing pawns
            if (f == 0 || checkFile(f - 1, opponent) == 0) {        // checks the file on the left
                if (f == 7 || checkFile(f + 1, opponent) == 0) {    // checks the file on the right
                    if (current == 'P'){
                        E += (value_passedPawn * 18) / (8 - (r + 1));   // evaluation formula for white
                    } else {
                        E -= (value_passedPawn * 18) /r;                // evaluation formula for black
                    }
                }
            }
        }

        // this part checks for isolated pawns, which are pawns that can not be protected by
        // other pawns.
        // it checks the files on both sides for friendly pawns
        if (f == 0 || checkFile(f - 1, current) == 0) {
            if (f == 7 || checkFile(f + 1, current) == 0) {
                E -= value_isolatedPawn * m;
            }
        }

        // this part searches stacked pawns, which means there are several pawns on the same file
        n = checkFile(f, current) - 1;
        E -= ((n * value_stackedPawn + ((n*(n-1))/2) * 0.2 * value_stackedPawn)/2) * m;

        // this code evaluates central pawns.
        // the center is defined as the square with the corners c4 and f5
        if (f>=2 && f<=5){
            if (current == 'p' && r<=4 && r>=3){
                E -= value_centralPawn;
            } else if(current == 'P' && r<=4 && r>=3) {
                E += value_centralPawn;
            }
        }

        // this part is only active in the endgame, meaning there are almost no
        // pieces left on the board.
        // the method evaluates advanced pawns positive, regardless of the center
        if (endgame) {
            if (current == 'P') {
                E += value_advancedPawn * r;
            } else {
                E -= value_advancedPawn * (7-r);
            }
        }

        // the following code searches for pawnchains and evaluates them
        int tmp = 0;

        if (f!=7 && r!=7){
            if (board[r+1][f+1] == current){
                double val = value_pawnChain * m;
                if (current == 'P'){
                    val *= (1/(double)r);
                } else {
                    val *= (double)(1/(double)(6-r));
                }
                E += val;
            }
        }
        if (f!=0 && r!=7){
            if (board[r+1][f-1] == current){
                double val = value_pawnChain * m;
                if (current == 'P'){
                    val *= (1/(double)r);
                } else {
                    val *= (double)(1/(double)(6-r));
                }
                E += val;
            }
        }

    }


    public void knight(int r, int f) {
        // this method evaluates knights which are rather in the center instead of the edges
        int m = 1;

        if (board[r][f] == 'n') {m = -1;}

        if (f >= 2 && f <= 5) {
            if (r <= 5 && r >= 2){
                E += value_centralKnight * m;
            }
        }
    }


    public void rook(int r, int f){
        // this method evaluates rooks on the 7th (or 2nd) rank and
        // rooks in the center of the board

        int m = 1;
        if (board[r][f] == 'r') {m = -1;}

        if (f >= 2 && f <= 5) {
            E += value_centralRook * m;
        }

        if (r == 6 && board[r][f] == 'R') {
            E += value_seventhRankRook;
        } else if (r == 1 && board[r][f] == 'r') {
            E -= value_seventhRankRook;
        }

    }


    public void king(int r, int f) {
        // the following code evaluates the king in the endgame and the king-safety
        // specifically, the king is supposed to lead the pawns in the endgame

        int m = 1;
        int r_ = r;

        if (board[r][f] == 'k') {
            m = -1;
            r = (r - 7) * m;
        }

        if (endgame){

            String[] s = getSurroundings(r_, f, true);

            if (m == 1){
                if (s[0].equals('P'+ "")){
                    E += value_kingLead;
                }
                if (s[1].equals('P'+ "")){
                    E += value_kingLead;
                }
                if (s[2].equals('P'+ "")){
                    E += value_kingLead;
                }
            } else {
                if (s[5].equals('p'+ "")){
                    E -= value_kingLead;
                }
                if (s[6].equals('p'+ "")){
                    E -= value_kingLead;
                }
                if (s[7].equals('p'+ "")){
                    E -= value_kingLead;
                }
            }
        } else {
            // in any gamephase other than the endgame, the king is supposed to
            // stay in the corners, and it should be sheltered by pawns
            if (r == 0 && (f == 0 || f == 1 || f == 6 || f == 7)) {
                E += value_kingSafety * m;
            } else if (r == 1 && (f == 0 || f == 7)) {
                E += value_kingSafety * m * (1 / 6.0);
            } else if (r == 0 && f == 2) {
                E += value_kingSafety * m * (1 / 5.0);

            }
        }
    }



    public void check(char[][] board, int r, int f) {

        if (Character.toLowerCase(board[r][f]) == 'p'){
            pawn(r, f);
        } else if (Character.toLowerCase(board[r][f]) == 'n') {
            knight(r, f);
        } else if (Character.toLowerCase(board[r][f]) == 'r') {
            rook(r, f);
        } else if (Character.toLowerCase(board[r][f]) == 'k') {
            king(r, f);
        }

    }
}