import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyMouseListener implements MouseListener {

    private final int squareSize;
    private final MyPanel panel;

    private int selectedFile = -1, selectedLine = -1;

    public MyMouseListener(int squareSize, MyPanel panel) {
        this.squareSize = squareSize;
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }


    @Override
    public void mousePressed(MouseEvent e) {    //General selection of Pieces

        if (Main.promotionPanel.isVisible()) {    //Cannot move or select Pieces when a Piece is supposed to be promoted
            return;
        }

        int temp1, temp2;
        boolean nameInProcess = true;   //Used to reduce compiler load
        temp1 = e.getX() / squareSize;  //Gets clicked file
        temp2 = e.getY() / squareSize;  //Gets clicked line

        if (selectedLine == -1 && Piece.board[temp2][temp1] != null) { //If no piece is selected
            selectedFile = temp1;
            selectedLine = temp2;

            panel.setSelectedFile(selectedFile);
            panel.setSelectedLine(selectedLine);
            panel.repaint();
            return;

        } else if (selectedLine == -1 && Piece.board[temp2][temp1] == null) {    //If no piece is selected and an empty square is clicked
            return;
        }
        if (Piece.board[selectedLine][selectedFile] == null) { //Null-check
            nameInProcess = false;
        }
        if (selectedFile == temp1 && selectedLine == temp2) {   //Equals-check -> can't move a Piece to where it is
            nameInProcess = false;
            selectedFile = -1;
            selectedLine = -1;
        }
        if (nameInProcess) {

            if (true) {   //Checks if the move is valid
                System.out.println(Piece.containsMove(Piece.board[selectedLine][selectedFile].getValidMoves((selectedLine << 3) + selectedFile, Piece.board[selectedLine][selectedFile].isWhite), (temp2 << 3) + temp1));
                System.out.println(Knight.isMoveValid((selectedLine << 3) + selectedFile, (temp2 << 3) + temp1, !Piece.board[selectedLine][selectedFile].isWhite));

                Piece.board[temp2][temp1] = Piece.board[selectedLine][selectedFile];//Makes the move
                Piece.board[selectedLine][selectedFile] = null;
                Piece.board[temp2][temp1].pos = ((temp2 << 3) + temp1);
                Main.updateSBoard();

                selectedFile = -1;
                selectedLine = -1;

            } else if (Piece.board[temp2][temp1] != null) {   //Selects the clicked Piece since moving the selected Piece there is invalid
                selectedFile = temp1;
                selectedLine = temp2;

            } else {  //If clicked square is null and moving there is invalid selected Piece is unselected
                selectedFile = -1;
                selectedLine = -1;
            }
        }

        panel.setSelectedFile(selectedFile);
        panel.setSelectedLine(selectedLine);
        panel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}