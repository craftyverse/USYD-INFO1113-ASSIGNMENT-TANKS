package Tanks;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.*;
import processing.data.JSONObject;

public class GameBackground extends PApplet {
  PImage background_image;
  PImage windRightImage;
  PImage windLeftImage;
  AppUtils appUtils;
  String levelBackgroundImageFile;
  String levelTreeFile;
  JSONObject levelPlayerColors;
  ArrayList<Integer> levelTerrain;
  ArrayList<Integer> levelTrees;
  ArrayList<Integer> levelTanks;
  ArrayList<Float> smoothedLevelTerrain;
  ArrayList<Float> curvedLevelTerrain;
  HashMap<String, GamePlayer> gamePlayers;
  GameTerrain level1;
  GamePlayer currentPlayer;
  String currentPlayerName;
  int currentPlayerNameIndex = 0;
  float tankTurrentAngle = 0f;
  ArrayList<TankProjectile> tankProjectiles;
  float windAcceleration = generateInitialWindValue() * (0.03f);
  float windVelocityX = (float) (windAcceleration * ((millis() / 1000.0) * (millis() / 1000.0)));

  public void setup() {

    appUtils = new AppUtils(this);
    level1 = new GameTerrain(this, this, windAcceleration, windVelocityX);

    // Load level 1 background image, tree file, and player colors
    levelBackgroundImageFile = appUtils.readLevelConfig("config.json", 1, "background");
    levelTreeFile = appUtils.readLevelConfig("config.json", 1, "trees");
    levelPlayerColors = appUtils.readPlayerColors("config.json");

    // Load necessary player information
    levelTerrain = level1.loadTerrainObjects("level1Terrain.txt");
    levelTrees = level1.loadTerrainObjects("level1Tree.txt");
    levelTanks = level1.loadTerrainObjects("level1Tanks.txt");

    // Calculate smoothed terrain
    smoothedLevelTerrain = level1.calculateMovingAverageInt(levelTerrain);
    curvedLevelTerrain = level1.calculateMovingAverageFloat(smoothedLevelTerrain);

    // Set up game players
    level1.setGamePlayers(curvedLevelTerrain, levelTanks, levelPlayerColors);
    currentPlayer = level1.getGamePlayers().get("A");
    System.out.println(currentPlayer.getPlayerName());

  }

  public void settings() {
    size(864, 640);
  }

  public void draw() {

    // Background
    background_image = loadImage(levelBackgroundImageFile);
    windLeftImage = loadImage("wind-1.png");
    windRightImage = loadImage("wind.png");

    image(background_image, 0, 0);

    // Render Terrain
    level1.drawTerrain(curvedLevelTerrain);

    // Render Trees
    level1.drawTrees(curvedLevelTerrain, levelTrees, levelTreeFile);

    // Render Tanks
    level1.drawTanks(curvedLevelTerrain, levelTanks, levelPlayerColors);

    // Render player names
    textSize(16);
    fill(0);
    text(currentPlayer.getPlayerName() + "'s turn", 10, 35);

    // Render player fuel
    PImage fuel = loadImage("fuel.png");
    fuel.resize(30, 30);
    image(fuel, 160, 10);
    text(currentPlayer.getPlayerTankFuel(), 190, 35);

    // Render player health
    text("Health: ", 380, 35);
    String[] colorArr = currentPlayer.getPlayerTankColor().split(",");
    fill(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2]));
    rect(440, 19, 100, 20);
    fill(0);
    text(currentPlayer.getPlayerTankHealth(), 545, 35);

    // Render Power level
    fill(0);
    text("Power: ", 380, 65);
    text((int) (currentPlayer.getPlayerTank().projectilePower * 10), 440, 65);
    System.out.println("Wind acceleration: " + currentPlayer.getPlayerTank().getWindAcceleration());

    if (currentPlayer.getPlayerTank().getWindAcceleration() > 0) {
      image(windRightImage, 700, 10);
      text(round(abs(currentPlayer.getPlayerTank().getWindAcceleration() * 100)), 770, 45);
    } else if (currentPlayer.getPlayerTank().getWindAcceleration() < 0) {
      image(windLeftImage, 700, 10);
      text(round(abs(currentPlayer.getPlayerTank().getWindAcceleration() * 100)), 770, 45);
    }
  }

  public void updateTankFuel() {
    if (currentPlayer.getPlayerTankFuel() > 0 || currentPlayer.getPlayerTankFuel() <= 250) {
      int fuelRate = 0;
      fuelRate++;
      currentPlayer.setPlayerTankFuel(currentPlayer.getPlayerTankFuel() - fuelRate);
    }
  }

  public void keyPressed() {
    String[] playersNames = level1.getGamePlayers().keySet().toArray(new String[0]);
    if (key == CODED) {
      if (keyCode == RIGHT) {
        currentPlayer.getPlayerTank().isMovingRight = true;
        if (currentPlayer.getPlayerTankFuel() > 0) {
          currentPlayer.getPlayerTank().moveTank();
          updateTankFuel();
        }

      }

      if (keyCode == LEFT) {
        currentPlayer.getPlayerTank().isMovingLeft = true;
        if (currentPlayer.getPlayerTankFuel() > 0) {
          currentPlayer.getPlayerTank().moveTank();
          updateTankFuel();
        }

      }

      if (keyCode == DOWN && currentPlayer.getPlayerTank().rotationAngle < 0) {
        System.out.println("DOWN");
        currentPlayer.getPlayerTank().isRotatingRight = true;
        currentPlayer.getPlayerTank().rotationAngle += radians(5);
      }

      if (keyCode == UP && currentPlayer.getPlayerTank().rotationAngle > -PI) {
        System.out.println("UP");
        currentPlayer.getPlayerTank().isRotatingRight = true;
        currentPlayer.getPlayerTank().rotationAngle -= radians(5);
      }
    }

    if (key == 'w') {
      if (currentPlayer.getPlayerTank().projectilePower < 9)
        currentPlayer.getPlayerTank().projectilePower += 1;
    }

    if (key == 's') {
      if (currentPlayer.getPlayerTank().projectilePower > 0)
        currentPlayer.getPlayerTank().projectilePower -= 1;
    }

    if (key == ' ') {
      currentPlayer.getPlayerTank().keyPressed();
      switchPlayer(playersNames);
    }
  }

  public void keyReleased() {
    if (key == CODED) {
      if (keyCode == RIGHT) {
        currentPlayer.getPlayerTank().isMovingRight = false;
      }

      if (keyCode == LEFT) {
        currentPlayer.getPlayerTank().isMovingLeft = false;
      }
    }
  }

  public void switchPlayer(String[] playersNames) {
    currentPlayerNameIndex = (currentPlayerNameIndex + 1) % playersNames.length;
    currentPlayerName = playersNames[currentPlayerNameIndex];
    currentPlayer = level1.getGamePlayers().get(currentPlayerName);
  }

  public int generateInitialWindValue() {
    int windValue = (int) random(-35, 35);
    return windValue;
  }
}
