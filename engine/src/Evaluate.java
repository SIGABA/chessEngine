public class Evaluate {
    static double value_passedPawn = 0.5;
    static double value_isolatedPawn = 0.5;
    static double value_stackedPawn = 0.5;

    static double E = 0.0;

    public static double evaluate(char[][] board){

        for (int r = 0; r<8; r++){
            for (int f = 0; f<8; f++){

                check(board, r, f);

            }
        }
        return E;
    }


    public static int checkFile(char[][] board, int file, char character){
        int counter = 0;

        for (int r = 0; r<8; r++){
            if (board[r][file] == character){
                counter++;
            }
        }
        return counter;
    }


    public static void pawn(char[][] board, int r, int f) {
        double n = 0;

        if (board[r][f] == 'P') {
            // passed
            if (checkFile(board, f, 'p') == 0) {
                if (f == 0 || checkFile(board, f - 1, 'p') == 0) {
                    if (f == 7 || checkFile(board, f + 1, 'p') == 0) {
                        System.out.println(board[r][f]);
                        E += (value_passedPawn * 18) / (8 - (r + 1));
                        System.out.println(E);
                    }
                }
            }

            // isolated white
            if (f == 0 || checkFile(board, f - 1, 'P') == 0) {
                if (f == 7 || checkFile(board, f + 1, 'P') == 0) {
                    E -= value_isolatedPawn;
                }
            }

            // stacked white
            n = checkFile(board, f, 'P') - 1;
            E -= (n * value_stackedPawn + ((n*(n-1))/2) * 0.2 * value_stackedPawn)/2;
            // advanced white
            // chain white

        }
        if (board[r][f] == 'p') {
            // passed black
            if (checkFile(board, f, 'P') == 0) {
                if (f == 0 || checkFile(board, f - 1, 'P') == 0) {
                    if ((f == 7 || checkFile(board, f + 1, 'P') == 0)) {
                        System.out.println(board[r][f]);
                        E -= (value_passedPawn * 18) /r;
                        System.out.println(E);
                    }
                }
            }
            // isolated black
            if (f == 0 || checkFile(board, f - 1, 'p') == 0) {
                if (f == 7 || checkFile(board, f + 1, 'p') == 0) {
                    E += value_isolatedPawn;
                }
            }
            // stacked black
            n = checkFile(board, f, 'p') - 1;
            E += (n * value_stackedPawn + ((n*(n-1))/2) * 0.2 * value_stackedPawn)/2;
            // advanced black
            // chain black
        }
    }


    public static void check(char[][] board, int r, int f) {

        pawn(board, r, f);

    }
}