package Tanks;

public class GamePlayer {
  private String playerName;
  private String playerTextColor;
  private String playerTankColor;
  private int playerTankFuel;
  private int playerTankHealth;
  private int playerTankScore;
  private GameTank playerTank;

  public GamePlayer(String playerName, String playerTextColor, String playerTankColor, GameTank playerTank) {
    this.playerName = playerName;
    this.playerTextColor = playerTextColor;
    this.playerTankColor = playerTankColor;
    this.playerTank = playerTank;
    this.playerTankFuel = 250;
    this.playerTankHealth = 100;

  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public String getPlayerTextColor() {
    return playerTextColor;
  }

  public void setPlayerTextColor(String playerTextColor) {
    this.playerTextColor = playerTextColor;
  }

  public String getPlayerTankColor() {
    return playerTankColor;
  }

  public void setPlayerTankColor(String playerTankColor) {
    this.playerTankColor = playerTankColor;
  }

  public int getPlayerTankFuel() {
    return playerTankFuel;
  }

  public void setPlayerTankFuel(int playerTankFuel) {
    this.playerTankFuel = playerTankFuel;
  }

  public int getPlayerTankHealth() {
    return playerTankHealth;
  }

  public void setPlayerTankHealth(int playerTankHealth) {
    this.playerTankHealth = playerTankHealth;
  }

  public int getPlayerTankScore() {
    return playerTankScore;
  }

  public void setPlayerTankScore(int playerTankScore) {
    this.playerTankScore = playerTankScore;
  }

  public GameTank getPlayerTank() {
    return playerTank;
  }

  public void setPlayerTank(GameTank playerTank) {
    this.playerTank = playerTank;
  }
}