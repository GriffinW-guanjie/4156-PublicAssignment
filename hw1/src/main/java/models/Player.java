package models;

public class Player {

  private char type;

  private int id;

  public char getType() {
    return type;
  }

  public void setType(char type) {
    this.type = type;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Initialize a player with type and id.
   * 
   * @param type Player's piece's type
   * @param id Player's id
   */
  public Player(char type, int id) {
    super();
    this.type = type;
    this.id = id;
  }

}
