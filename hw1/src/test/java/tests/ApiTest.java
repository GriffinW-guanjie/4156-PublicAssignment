package tests;

import com.google.gson.Gson;
import controllers.PlayGame;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Message;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class)
public class ApiTest {

  /**
   * test class for endpoints.
   */

  @BeforeAll
  public static void init() {
    // Start Server
    String[] nullString = new String[2];
    PlayGame.main(nullString);
    System.out.println("Before All");
  }
  
  /**
   * test the server before every test.
   */
    
  @BeforeEach
  public void startNewGame() {
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in this method.
    Unirest.get("http://localhost:8080/").asString();
    //int restStatus = response.getStatus();
    System.out.println("Before Each");
  }
    
  @Test
  @Order(1)
  public void newGameTest() {
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
    // Check assert statement (New Game has started)
    Assertions.assertEquals(restStatus, 200);
    System.out.println("Test New Game");
  }
  
  @Test
  @Order(2)
  public void startGameTest() {
    // Create a POST request to startgame endpoint and get the body
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = response.getBody();
    System.out.println("Start Game Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(false, jsonObject.get("gameStarted"));
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    Assertions.assertEquals('X', player1.getType());
    System.out.println("Test Start Game");
  }
  
  @Test
  @Order(3)
  public void moveTest1() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2/").field("x", "0").field("y", "1").asString();
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(false, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(401, code);
    System.out.println("Test move before Start Game");
  }
  
  @Test
  @Order(4)
  public void moveTest2() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2/").field("x", "0").field("y", "1").asString();
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(false, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(402, code);
    System.out.println("Test P2 move first");
  }
  
  @Test
  @Order(5)
  public void moveTest3() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "1").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2/").field("x", "0").field("y", "1").asString();
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(false, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(403, code);
    System.out.println("Test move on token place");
  }
  
  @Test
  @Order(6)
  public void moveTest4() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "1").asString();
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(100, code);
    System.out.println("Test P1 move first");
  }
  
  @Test
  @Order(7)
  public void moveTest5() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "1").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "1").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(false, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(402, code);
    System.out.println("Test P1 move twice");
  }
  
  @Test
  @Order(8)
  public void drawTest() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "0").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "2").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "1").field("y", "2").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "2").field("y", "2").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "1").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(300, code);
    System.out.println("Test draw");
  }
  
  @Test
  @Order(9)
  public void winTest1() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "1").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "2").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(200, code);
    System.out.println("Test win");
  }
  
  @Test
  @Order(10)
  public void winTest2() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "1").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "2").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "1").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "2").field("y", "1").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "1").field("y", "2").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(200, code);
    System.out.println("Test win");
  }
  
  @Test
  @Order(11)
  public void winTest3() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "1").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "2").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(200, code);
    System.out.println("Test win");
  }
  
  @Test
  @Order(12)
  public void winTest4() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "0").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "1").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "1").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "0").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(200, code);
    System.out.println("Test win");
  }
  
  @Test
  @Order(13)
  public void winTest5() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "0").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "1").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "0").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "1").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(200, code);
    System.out.println("Test win");
  }
  
  @Test
  @Order(14)
  public void winTest6() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "2").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "0").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "1").field("y", "2").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "0").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "2").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(200, code);
    System.out.println("Test win");
  }
  
  @Test
  @Order(15)
  public void winTest7() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "0").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "0").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "1").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "0").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "2").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(200, code);
    System.out.println("Test win");
  }
  
  @Test
  @Order(16)
  public void winTest8() {
    Unirest.post("http://localhost:8080/startgame").field("type", "X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "2").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "2").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "1").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "0").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "0").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(200, code);
    System.out.println("Test win");
  }
  
  @Test
  @Order(17)
  public void p2Test() {
    Unirest.post("http://localhost:8080/startgame").field("type", "O").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "0").field("y", "2").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "2").asString();
    Unirest.post("http://localhost:8080/move/1/").field("x", "1").field("y", "1").asString();
    Unirest.post("http://localhost:8080/move/2/").field("x", "1").field("y", "0").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1/").field("x", "2").field("y", "0").asString();    
    String responseBody = response.getBody();
    System.out.println("Move Response: " + responseBody);
    JSONObject jsonObject = new JSONObject(responseBody);
    Assertions.assertEquals(true, jsonObject.get("moveValidity"));
    Gson gson = new Gson();
    Message message = gson.fromJson(jsonObject.toString(), Message.class);
    int code = message.getCode();
    Assertions.assertEquals(200, code);
    System.out.println("Test win");
  }
    
  @AfterEach
  public void finishGame() {
    System.out.println("After Each");
  }
    
  /**
   * This method runs only once after all the test cases have been executed.
   */
  @AfterAll
  public static void close() {
    // Stop Server
    PlayGame.stop();
    System.out.println("After All");
  }

}
