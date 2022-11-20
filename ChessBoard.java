import java.util.*;
import java.lang.*;

public class ChessBoard
{
    private static Square board[][] = new Square[8][8];
    
    /*
    8 | r  n  b  q  k  b  n  r  
    7 | p  p  p  p  p  p  p  p  
    6 | .  .  .  .  .  .  .  .  
    5 | .  .  .  .  .  .  .  .  
    4 | .  .  .  .  .  .  .  .  
    3 | .  .  .  .  .  .  .  .  
    2 | P  P  P  P  P  P  P  P  
    1 | R  N  B  Q  K  B  N  R  
      |_______________________
        A  B  C  D  E  F  G  H
        
    ////////////////////////////
    
    0 | r  n  b  q  k  b  n  r  
    1 | p  p  p  p  p  p  p  p  
    2 | .  .  .  .  .  .  .  .  
    3 | .  .  .  .  .  .  .  .  
    4 | .  .  .  .  .  .  .  .  
    5 | .  .  .  .  .  .  .  .  
    6 | P  P  P  P  P  P  P  P  
    7 | R  N  B  Q  K  B  N  R  
      |_______________________
        0  1  2  3  4  5  6  7 
    */
    
    private static int player = 1; // player 1 is white, player 2 is black
    private static int winner = 0;
    public static Scanner scan = new Scanner(System.in);
    
    private static boolean validToChoose;
    private static boolean validToMove;
    
    private static Square squareToMoveFrom;
    private static Square squareToMoveTo;
    
    private static final String alphabet = "ABCDEFGH";
    private static final String bar = "\uFF5C";
    
    public static void main(String[] args)
    {
        setBoard();
        while (winner == 0)
        {
            printBoard();
            System.out.println("Player " + player + "'s turn.");
            getSTMF();
            getSTMT();
            makeMove();
            checkForWin();
        }
        printBoard();
        System.out.println("--------------------");
        System.out.println("The winner is: " + winner);
        System.out.println("--------------------");
    }
    
    public static void getSTMF()
    {
        System.out.println("What piece to move?");
        String pieceToMoveInput = scan.nextLine();
        while (!(canConvert(pieceToMoveInput)))
        {
            System.out.println("Invalid format. Enter in the format LETTERNUMBER (ex. D4)");
            System.out.println("What piece to move?");
            pieceToMoveInput = scan.nextLine();
        }
        String pieceToMove = convertInput(pieceToMoveInput,"from");
        if (player == 1)
        {
            if ((!(squareToMoveFrom.isEmpty())) && (squareToMoveFrom.getPiece().isWhite())) {validToChoose = true;}
            else validToChoose = false;
        }
        else
        {
            if ((!(squareToMoveFrom.isEmpty())) && !(squareToMoveFrom.getPiece().isWhite())) {validToChoose = true;}
            else validToChoose = false;
        }
        while (!validToChoose)
        {
            System.out.print("Invalid choice. ");
            System.out.println("What piece to move?");
            pieceToMoveInput = scan.nextLine();
            pieceToMove = convertInput(pieceToMoveInput,"from");
            if (player == 1)
            {
                if ((!(squareToMoveFrom.isEmpty())) && (squareToMoveFrom.getPiece().isWhite())) {validToChoose = true;}
                else validToChoose = false;
            }
            else
            {
                if ((!(squareToMoveFrom.isEmpty())) && !(squareToMoveFrom.getPiece().isWhite())) {validToChoose = true;}
                else validToChoose = false;
            }
        }
    }
    
    public static void getSTMT()
    {
        System.out.println("Where to move?");
        String whereToMoveInput = scan.nextLine();
        if (whereToMoveInput.equals("undo"))
        {
            getSTMF();
            System.out.println("Where to move?");
            whereToMoveInput = scan.nextLine();
        }
        while (!(canConvert(whereToMoveInput)))
        {
            System.out.println("Invalid format. Enter in the form LETTERNUMBER (ex. D4)");
            System.out.println("Where to move?");
            whereToMoveInput = scan.nextLine();
        }
        String whereToMove = convertInput(whereToMoveInput,"to");
        validToMove = ((player == 1) &&
                       (squareToMoveTo.isEmpty() || !(squareToMoveTo.getPiece().isWhite())) &&
                       (squareToMoveFrom.getPiece().canMove(squareToMoveFrom, squareToMoveTo)))
                       ||
                       ((player == 2) &&
                       (squareToMoveTo.isEmpty() || squareToMoveTo.getPiece().isWhite()) &&
                       (squareToMoveFrom.getPiece().canMove(squareToMoveFrom, squareToMoveTo)));
        while (!validToMove)
        {
            System.out.print("Invalid move. ");
            System.out.println("Where to move?");
            whereToMoveInput = scan.nextLine();
            whereToMove = convertInput(whereToMoveInput,"to");
            validToMove = ((player == 1) &&
                          ((squareToMoveTo.isEmpty() || squareToMoveTo.getPiece().isWhite() == false)) &&
                          (squareToMoveFrom.getPiece().canMove(squareToMoveFrom, squareToMoveTo)))
                          ||
                          ((player == 2) &&
                          ((squareToMoveTo.isEmpty() || squareToMoveTo.getPiece().isWhite())) &&
                          (squareToMoveFrom.getPiece().canMove(squareToMoveFrom, squareToMoveTo)));
        }
    }
    
    public static boolean canConvert(String s)
    {
        return ((s.length() == 2) &&
                (Character.isLetter(s.charAt(0))) &&
                (Character.isDigit(s.charAt(1))) &&
                (alphabet.contains(s.substring(0,1).toUpperCase())) &&
                ((Integer.valueOf(s.substring(1)) >= 0) && (Integer.valueOf(s.substring(1)) <= 8)));
    }
    
    public static void makeMove()
    {
        squareToMoveTo.setPiece(squareToMoveFrom.getPiece());
        squareToMoveTo.setEmpty(false);
        squareToMoveTo.getPiece().moved();
        squareToMoveFrom.nullPiece();
        squareToMoveFrom.setEmpty(true);
        
        if (player == 1) player++;
        else if (player == 2) player--;
    }
    
    public static String convertInput(String s, String toOrFrom)
    {
        String squareOnBoard = "";
        //
        String r = s.substring(1); // ex. 2
        int row = board.length - Integer.valueOf(r);
        squareOnBoard += String.valueOf(row);
        //
        String c = s.substring(0,1).toUpperCase(); // ex. D
        int col = alphabet.indexOf(c); // String alphabet = "ABCDEFGH" (01234567)
        squareOnBoard += String.valueOf(col);
        
        if (toOrFrom.equals("from"))
        {
            squareToMoveFrom = board[row][col];
        }
        else if (toOrFrom.equals("to"))
        {
            squareToMoveTo = board[row][col];
        }
        //
        return squareOnBoard;
    }
    
    public static void setBoard()
    {
        // black pieces
        board[0][0] = new Square(0, 0, new Rook(false));
        board[0][1] = new Square(0, 1, new Knight(false));
        board[0][2] = new Square(0, 2, new Bishop(false));
        board[0][3] = new Square(0, 3, new Queen(false));
        board[0][4] = new Square(0, 4, new King(false));
        board[0][5] = new Square(0, 5, new Bishop(false));
        board[0][6] = new Square(0, 6, new Knight(false));
        board[0][7] = new Square(0, 7, new Rook(false));
        
        // black pawns
        for (int i = 0; i < 8; i++)
        {
            board[1][i] = new Square(1, i, new Pawn(false));
        }
        
        // null spaces
        for (int i = 2; i < 6; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j] = new Square(i, j);
            }
        }
        
        // white pawns
        for (int i = 0; i < 8; i++)
        {
            board[6][i] = new Square(6, i, new Pawn(true));
        }
        
        // white pieces
        board[7][0] = new Square(7, 0, new Rook(true));
        board[7][1] = new Square(7, 1, new Knight(true));
        board[7][2] = new Square(7, 2, new Bishop(true));
        board[7][3] = new Square(7, 3, new Queen(true));
        board[7][4] = new Square(7, 4, new King(true));
        board[7][5] = new Square(7, 5, new Bishop(true));
        board[7][6] = new Square(7, 6, new Knight(true));
        board[7][7] = new Square(7, 7, new Rook(true));
    }
    
    public static void printBoard()
    {
        System.out.println("    _A__B__C__D__E__F__G__H_");
        for (int i = 0; i < 8; i++)
        {
            System.out.print((8 - i) + " " + bar + " ");
            for (int j = 0; j < 7; j++)
            {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println(board[i][7] + " " + bar + " " + (8 - i));
        }
        System.out.println("  " + bar + "________________________" + bar + "\n     A  B  C  D  E  F  G  H");
    }
    
    public static Square getSquare(int x, int y)
    {
        return board[x][y];
    }
    
    public static void checkForWin()
    {
        //if kings are dead set winner to whichever king is alive
        boolean whiteKing = false;
        boolean blackKing = false;
        
        for (Square[] row : board)
        {
            for (Square s : row)
            {
                if (s.getPiece() instanceof King)
                {
                    if (s.getPiece().isWhite())
                    {
                        whiteKing = true;
                    }
                    else
                    {
                        blackKing = true;
                    }
                }
            }
        }
        
        if (!(whiteKing)) winner = 2;
        if (!(blackKing)) winner = 1;
    }
}

class Square
{
    private int x;
    private int y;
    private Piece piece;
    private boolean empty;
    
    // populated square
    public Square(int x, int y, Piece piece)
    {
        this.x = x;
        this.y = y;
        this.piece = piece;
        this.empty = false;
    }
    
    // unpopulated square
    public Square(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.piece = null;
        this.empty = true;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public boolean isEmpty()
    {
        return empty;
    }
    
    public Piece getPiece()
    {
        return piece;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public void setEmpty(boolean b)
    {
        this.empty = b;
    }
    
    public void setPiece(Piece piece)
    {
        this.piece = piece;
    }
    
    public String toString()
    {
        if (empty) {return ".";}
        else {return piece.toString();}
    }
    
    public void nullPiece()
    {
        this.piece = null;
    }
}

class Piece
{
    protected boolean white;
    protected boolean alive = true;
    protected boolean hasMoved = false;
    
    public Piece(boolean isWhite)
    {
        white = isWhite;
    }
    
    public boolean isWhite()
    {
        return white;
    }
    
    public boolean isAlive()
    {
        return alive;
    }
    
    public void setWhite(boolean white)
    {
        this.white = white;
    }
    
    public void setAlive(boolean isAlive)
    {
        this.alive = isAlive;
    }
    
    public String toString()
    {
        return " ";
    }
    
    public void moved()
    {
        hasMoved = true;
    }
    
    // never called, subclasses have overridden methods
    public boolean canMove(Square squareFrom, Square squareTo)
    {
        return false;
    }
    
    public boolean canMoveSetup(Square s)
    {
        if (s.isEmpty())
        {
            return true;
        }
        else if ((white && s.getPiece().isWhite()) ||
                 (!(white) && !(s.getPiece().isWhite())))
        {
            return false;
        }
        return true;
    }
    
    // called in rook's canMove and queen's canMove
    public boolean rookCanMove(Square squareFrom, Square squareTo)
    {
        if (squareFrom.getX() == squareTo.getX())
        {
            if (squareFrom.getY() < squareTo.getY())
            {
                for (int i = squareFrom.getY() + 1; i < squareTo.getY(); i++)
                {
                    if (!(chessBoard.getSquare(squareFrom.getX(), i).isEmpty()))
                    {
                        return false;
                    }
                }
                return true;
            }
            else if (squareFrom.getY() > squareTo.getY())
            {
                for (int i = squareFrom.getY() - 1; i > squareTo.getY(); i--)
                {
                    if (!(chessBoard.getSquare(squareFrom.getX(), i).isEmpty()))
                    {
                        return false;
                    }
                }
                return true;
            }
            else return false;
        }
        else if (squareFrom.getY() == squareTo.getY())
        {
            if (squareFrom.getX() < squareTo.getX())
            {
                for (int i = squareFrom.getX() + 1; i < squareTo.getX(); i++)
                {
                    if (!(chessBoard.getSquare(i, squareFrom.getY()).isEmpty()))
                    {
                        return false;
                    }
                }
                return true;
            }
            else if (squareFrom.getX() > squareTo.getX())
            {
                for (int i = squareFrom.getX() - 1; i > squareTo.getX(); i--)
                {
                    if (!(chessBoard.getSquare(i, squareFrom.getY()).isEmpty()))
                    {
                        return false;
                    }
                }
                return true;
            }
            else return false;
        }
        else return false;
    }
    
    // called in rook's canMove and queen's canMove
    public boolean bishopCanMove(Square squareFrom, Square squareTo)
    {
        
        int startX = squareFrom.getX();
        int startY = squareFrom.getY();
        int endX = squareTo.getX();
        int endY = squareTo.getY();
        
        boolean goDown = endX > startX;
        boolean goUp = !(goDown);
        boolean goRight = endY > startY;
        boolean goLeft = !(goRight);

        if (!(Math.abs(endY - startY) - Math.abs(endX - startX) == 0)) {return false;} // ensures diagonal movement
        
        if (goDown && goRight)
        {
            for (int x = startX + 1, y = startY + 1; x < endX || y < endY; x++, y++)
            {
                if (!(chessBoard.getSquare(x,y).isEmpty()))
                {
                    return false;
                }
            }
            return true;
        }
        else if (goDown && goLeft)
        {
            for (int x = startX + 1, y = startY - 1; x < endX || y > endY; x++, y--)
            {
                if (!(chessBoard.getSquare(x,y).isEmpty()))
                {
                    return false;
                }
            }
            return true;
        }
        else if (goUp && goRight)
        {
            for (int x = startX - 1, y = startY + 1; x > endX || y < endY; x--, y++)
            {
                if (!(chessBoard.getSquare(x,y).isEmpty()))
                {
                    return false;
                }
            }
            return true;
        }
        else //if (goUp && goLeft)
        {
            for (int x = startX - 1, y = startY - 1; x > endX || y > endY; x--, y--)
            {
                if (!(chessBoard.getSquare(x,y).isEmpty()))
                {
                    return false;
                }
            }
            return true;
        }
    }
}

class Pawn extends Piece
{
    public Pawn(boolean isWhite)
    {
        super(isWhite);
    }
    
    public String toString()
    {
        if (white) {return "P";}
        else {return "p";}
    }
    
    public boolean canMove (Square squareFrom, Square squareTo)
    {
        int disToTravel = squareFrom.getX() - squareTo.getX();
        int disForTake = squareFrom.getY() - squareTo.getY(); //disForTake = 1: take to left, disForTake = -1: take to right
        
        if (white)
        {
            if ((disToTravel == 2) && (disForTake == 0) && (!(hasMoved)))
            {
                if (squareTo.isEmpty() && chessBoard.getSquare(squareTo.getX() + 1, squareTo.getY()).isEmpty())
                {
                    return true;
                }
                else return false;
            }
            else if (disToTravel == 1)
            {
                if ((disForTake == 1) || (disForTake == -1))
                {
                    if (!(squareTo.isEmpty()) && !(squareTo.getPiece().isWhite()))
                    {
                        return true;
                    }
                    else return false;
                }
                else if (disForTake == 0)
                {
                    if (squareTo.isEmpty())
                    {
                        return true;
                    }
                    else return false;
                }
                else return false;
            }
            else return false;
        }
        else
        {
            if ((disToTravel == -2) && (disForTake == 0) && (!(hasMoved)))
            {
                if (squareTo.isEmpty() && chessBoard.getSquare(squareTo.getX() - 1, squareTo.getY()).isEmpty())
                {
                    return true;
                }
                else return false;
            }
            else if (disToTravel == -1)
            {
                if ((disForTake == 1) || (disForTake == -1))
                {
                    if (!(squareTo.isEmpty()) && squareTo.getPiece().isWhite())
                    {
                        return true;
                    }
                    else return false;
                }
                else if (disForTake == 0)
                {
                    if (squareTo.isEmpty())
                    {
                        return true;
                    }
                    else return false;
                }
                else return false;
            }
            else return false;
        }
    }
}

class King extends Piece
{
    public King(boolean isWhite)
    {
        super(isWhite);
    }
    
    public String toString()
    {
        if (white) {return "K";}
        else {return "k";}
    }
    
    public boolean canMove(Square squareFrom, Square squareTo)
    {
        if (((Math.abs(squareFrom.getX() - squareTo.getY())) < 2) &&
            ((Math.abs(squareFrom.getY() - squareTo.getY())) < 2))
        {
            if ((squareTo.isEmpty()) ||
               ((white) && (!(squareTo.getPiece().isWhite()))) ||
               (!(white) && squareTo.getPiece().isWhite()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else return false;
    }
}

class Queen extends Piece
{
    public Queen(boolean isWhite)
    {
        super(isWhite);
    }
    
    public String toString()
    {
        if (white) {return "Q";}
        else {return "q";}
    }
    
    public boolean canMove(Square squareFrom, Square squareTo)
    {
        if (!(canMoveSetup(squareTo))) {return false;}
        if (rookCanMove(squareFrom, squareTo)) {return true;}
        return bishopCanMove(squareFrom, squareTo);
    }
}

class Bishop extends Piece
{
    public Bishop(boolean isWhite)
    {
        super(isWhite);
    }
    
    public String toString()
    {
        if (white) {return "B";}
        else {return "b";}
    }
    
    public boolean canMove(Square squareFrom, Square squareTo)
    {
        if (!(canMoveSetup(squareTo))) {return false;}
        return bishopCanMove(squareFrom, squareTo);
    }
}

class Knight extends Piece
{
    public Knight(boolean isWhite)
    {
        super(isWhite);
    }
    
    public String toString()
    {
        if (white) {return "N";}
        else {return "n";}
    }
    
    public boolean canMove(Square squareFrom, Square squareTo)
    {
        if (!(canMoveSetup(squareTo))) {return false;}
        //
        int xDif = Math.abs(squareFrom.getX() - squareTo.getX());
        int yDif = Math.abs(squareFrom.getY() - squareTo.getY());
        
        if (xDif * yDif == 2)
        {
            return true;
        }
        else return false;
    }
}

class Rook extends Piece
{
    public Rook(boolean isWhite)
    {
        super(isWhite);
    }
    
    public String toString()
    {
        if (white) {return "R";}
        else {return "r";}
    }
    
    public boolean canMove(Square squareFrom, Square squareTo)
    {
        if (!(canMoveSetup(squareTo)))
        {
            System.out.println("Can't move setup");
            return false;
        }
        return rookCanMove(squareFrom, squareTo);
    }
}