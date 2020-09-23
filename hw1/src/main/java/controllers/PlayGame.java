package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import org.eclipse.jetty.websocket.api.Session;



class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;

  

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {
    
    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });
    
    // Get the path to game.
    app.get("/newgame", ctx -> {
      ctx.result("http://" + ctx.host() + "/tictactoe.html");
    });
    
    GameBoard gameBoard = new GameBoard();
    
    
    //initialize the game board.
     
    
    app.post("/startgame", ctx -> {
      System.out.println(ctx.body());
      Gson gson = new Gson();
      gameBoard.setp1(ctx.body().charAt(ctx.body().length() - 1), 1);
      System.out.println(gson.toJson(gameBoard));
      sendGameBoardToAllPlayers(gson.toJson(gameBoard));
      
    });
    
    
    //set the board ready to play.
    
    
    app.get("/joingame", ctx -> {
      gameBoard.setp2(2);
      gameBoard.setGameStarted(true);
      gameBoard.setTurn(1);
      Gson gson = new Gson();
      ctx.result("http://" + ctx.host() + "/tictactoe.html?p=2");
      System.out.println(gson.toJson(gameBoard));
      sendGameBoardToAllPlayers(gson.toJson(gameBoard));
    });
    
    
    // set the board ready to play.
     
    
    app.post("/move/:playerId", ctx -> {
      System.out.println(Integer.parseInt(ctx.pathParam("playerId")));
      //System.out.println(ctx.body());
      System.out.println(ctx.body().split("&")[0].split("=")[1]
          + ctx.body().split("&")[1].split("=")[1]);
      int x = Integer.parseInt(ctx.body().split("&")[0].split("=")[1]);
      int y = Integer.parseInt(ctx.body().split("&")[1].split("=")[1]);
      Gson gson = new Gson();
      Message message = gameBoard.move(Integer.parseInt(ctx.pathParam("playerId")), x, y);
      if (message.isMoveValidity()) {
        sendGameBoardToAllPlayers(gson.toJson(gameBoard));
      }
      ctx.result(gson.toJson(message));
      if (message.getCode() != 100) {
        gameBoard.clearBoard();
      }
    });
    
    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
