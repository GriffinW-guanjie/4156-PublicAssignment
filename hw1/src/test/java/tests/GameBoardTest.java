package tests;


import models.GameBoard;
import models.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;



@TestMethodOrder(OrderAnnotation.class)
public class GameBoardTest {
  GameBoard testBoard = new GameBoard();
  Message message = new Message();
  
  @BeforeAll
  public static void init() {
    
  } 
  
  @BeforeEach
  public void initBoard() {
    testBoard.clearBoard();
  }
  
  @Test
  @Order(1)
  public void testType1() {
    testBoard.setp1('X', 1);
    testBoard.setp2(2);
    Assertions.assertEquals(testBoard.getP2().getType(), 'O');
  }
  
  @Test
  @Order(2)
  public void testType2() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    Assertions.assertEquals(testBoard.getP2().getType(), 'X');
  }
  
  @Test
  @Order(3)
  public void testMoveBeforeStart() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    message = testBoard.move(1, 0, 0);
    Assertions.assertEquals(message.getCode(), 401);
  }
  
  @Test
  @Order(4)
  public void testConsecutiveMove() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    message = testBoard.move(1, 0, 0);
    message = testBoard.move(1, 0, 1);
    Assertions.assertEquals(message.getCode(), 402);
  }
  
  @Test
  @Order(5)
  public void testPositionAvailability1() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    message = testBoard.move(1, 0, 0);
    message = testBoard.move(2, 0, 0);
    Assertions.assertEquals(message.getCode(), 403);
  }
  
  @Test
  @Order(6)
  public void testPositionAvailability2() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    message = testBoard.move(1, 0, 0);
    message = testBoard.move(2, 0, 1);
    Assertions.assertEquals(message.getCode(), 100);
  }
  
  @Test
  @Order(7)
  public void testWin1() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    testBoard.move(1, 0, 0);
    testBoard.move(2, 1, 0);
    testBoard.move(1, 0, 1);
    testBoard.move(2, 1, 1);
    message = testBoard.move(1, 0, 2);
    Assertions.assertEquals(message.getCode(), 200);
  }
  
  @Test
  @Order(8)
  public void testWin2() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    testBoard.move(1, 1, 0);
    testBoard.move(2, 0, 1);
    testBoard.move(1, 1, 1);
    testBoard.move(2, 0, 2);
    message = testBoard.move(1, 1, 2);
    Assertions.assertEquals(message.getCode(), 200);
  }
  
  @Test
  @Order(9)
  public void testWin3() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    testBoard.move(1, 2, 0);
    testBoard.move(2, 1, 0);
    testBoard.move(1, 2, 1);
    testBoard.move(2, 1, 1);
    message = testBoard.move(1, 2, 2);
    Assertions.assertEquals(message.getCode(), 200);
  }
  
  @Test
  @Order(10)
  public void testWin4() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    testBoard.move(1, 0, 0);
    testBoard.move(2, 1, 0);
    testBoard.move(1, 1, 1);
    testBoard.move(2, 1, 2);
    message = testBoard.move(1, 2, 2);
    Assertions.assertEquals(message.getCode(), 200);
  }
  
  @Test
  @Order(11)
  public void testWin5() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    testBoard.move(1, 0, 0);
    testBoard.move(2, 2, 2);
    testBoard.move(1, 1, 0);
    testBoard.move(2, 1, 1);
    message = testBoard.move(1, 2, 0);
    Assertions.assertEquals(message.getCode(), 200);
  }
  
  @Test
  @Order(12)
  public void testWin6() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    testBoard.move(1, 0, 1);
    testBoard.move(2, 2, 2);
    testBoard.move(1, 1, 1);
    testBoard.move(2, 1, 2);
    message = testBoard.move(1, 2, 1);
    Assertions.assertEquals(message.getCode(), 200);
  }
  
  @Test
  @Order(13)
  public void testWin7() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    testBoard.move(1, 0, 2);
    testBoard.move(2, 2, 1);
    testBoard.move(1, 1, 2);
    testBoard.move(2, 1, 1);
    message = testBoard.move(1, 2, 2);
    Assertions.assertEquals(message.getCode(), 200);
  }
  
  @Test
  @Order(13)
  public void testWin8() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    testBoard.move(1, 0, 2);
    testBoard.move(2, 2, 1);
    testBoard.move(1, 1, 1);
    testBoard.move(2, 1, 2);
    message = testBoard.move(1, 2, 0);
    Assertions.assertEquals(message.getCode(), 200);
  }
  
  @Test
  @Order(14)
  public void testDraw() {
    testBoard.setp1('O', 1);
    testBoard.setp2(2);
    testBoard.startGame();
    testBoard.move(1, 0, 0);
    testBoard.move(2, 0, 1);
    testBoard.move(1, 0, 2);
    testBoard.move(2, 1, 0);
    testBoard.move(1, 1, 2);
    testBoard.move(2, 1, 1);
    testBoard.move(1, 2, 0);
    testBoard.move(2, 2, 2);
    message = testBoard.move(1, 2, 1);
    Assertions.assertEquals(message.getCode(), 300);
  }

}