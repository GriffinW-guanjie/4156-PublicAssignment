package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;
  
  /**
   * Initialize a Gameboard.
   *
   */

  public GameBoard() {
    super();
    //this.p2 = new Player();
    gameStarted = false;
    turn = 0;
    initializeBoard();
    winner = 0;
    isDraw = false;
  }
  
  public void setp1(char type, int id) {
    p1 = new Player(type, id);
  }
  
  /**
   * p2 can't choose type.
   *
   */
  
  public void setp2(int id) {
    if (p1.getType() == 'X') {
      p2 = new Player('O', id);
    } else {
      p2 = new Player('X', id);
    }
  }
  
  private void initializeBoard() {
    this.boardState = new char[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        this.boardState[i][j] = 0;
      }
    }

  }

  public boolean isGameStarted() {
    return gameStarted;
  }

  public void setGameStarted(boolean gameStarted) {
    this.gameStarted = gameStarted;
  }

  public int getTurn() {
    return turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  public int getWinner() {
    return winner;
  }

  public void setWinner(int winner) {
    this.winner = winner;
  }

  public boolean isDraw() {
    return isDraw;
  }

  public void setDraw(boolean isDraw) {
    this.isDraw = isDraw;
  }

  public Player getP1() {
    return p1;
  }

  public Player getP2() {
    return p2;
  }

  public char[][] getBoardState() {
    return boardState;
  }
  
  /**
   *enumerate all the possible condition of winning.
   */
  
  private boolean checkWin() {
    if ((boardState[0][0] == boardState[0][1]) && (boardState[0][1] == boardState[0][2])
         && (boardState[0][0] != 0)
         || (boardState[1][0] == boardState[1][1]) && (boardState[1][1] == boardState[1][2])
         && (boardState[1][0] != 0)
         || (boardState[2][0] == boardState[2][1]) && (boardState[2][1] == boardState[2][2])
         && (boardState[2][0] != 0)
         || (boardState[0][0] == boardState[1][0]) && (boardState[1][0] == boardState[2][0])
         && (boardState[0][0] != 0)
         || (boardState[0][1] == boardState[1][1]) && (boardState[1][1] == boardState[2][1])
         && (boardState[0][1] != 0)
         || (boardState[0][2] == boardState[1][2]) && (boardState[1][2] == boardState[2][2])
         && (boardState[0][2] != 0)
         || (boardState[0][0] == boardState[1][1]) && (boardState[1][1] == boardState[2][2])
         && (boardState[0][0] != 0)
         || (boardState[0][2] == boardState[1][1]) && (boardState[1][1] == boardState[2][0])
         && (boardState[0][2] != 0)) {
      return true;
    } else {
      return false; 
    }
  } 
  
  private boolean checkDraw() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (boardState[i][j] == 0) {
          return false;
        }
      }
    }
    return true;
  }
  
  /**
   * public method to make a move.
   * 
   * @param id player id
   * @param x x coordinate
   * @param y y coordinate
   * @return the message
   */

  public Message move(int id, int x, int y) {
    Message message = new Message();
    if (!gameStarted) {
      message.setMoveValidity(false);
      message.setMessage("Game not started");
    } else if (id != turn) {
      message.setMoveValidity(false);
      message.setMessage("Not your turn");
    } else if (boardState[x][y] != 0) {
      message.setMoveValidity(false);
      message.setMessage("This square is already taken");
    } else {
      boardState[x][y] = id == 1 ? p1.getType() : p2.getType();
      turn = 3 - turn;
      if (checkWin()) {
        winner = id;
        message.setMoveValidity(true);
        message.setMessage("you win!");  
        message.setCode(200);
      } else if (checkDraw()) {
        isDraw = true;
        message.setMoveValidity(true);
        message.setMessage("Draw");
        message.setCode(300);
      } else {
        message.setMoveValidity(true);
        message.setCode(100);
      }
    }
    return message;
  }
  
  /**
   * clear the Class.
   */
  
  public void clearBoard() {
    gameStarted = false;
    turn = 0;
    initializeBoard();
    winner = 0;
    isDraw = false;
    p1 = null;
    p2 = null;
  }


}
