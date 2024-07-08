package asteroids;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.scene.text.Text;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class AsteroidsApplication extends Application{

    public static int WIDTH = 600;
    public static int HEIGHT = 400;
    
    //Object to Read Saved Data
    ReadUserData read = new ReadUserData();
    //List of Game Objects
    //Main Ship
    Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
    //Asteroid
    List<Asteroid> asteroids = new ArrayList<>();
    //Projectiles
    List<Projectile> projectiles = new ArrayList<>();
    //Ship rotation
    Map<KeyCode, Boolean> pressedKeys = new HashMap();
    
    
    private long lastShotTime = 0; // Time of the last projectile fired (in nanoseconds)
    private long lastSpeedIncrease = 0; // Time of the last speed increase (in nanoseconds)
    private final double interval =  1e9; // 1 second interval in nanoseconds    
    private int upgradePoints = 0;
    private int projectileRotation = 0;
    
    private double spawnChance = 0.5; // Initial spawn chance
    private final double spawnIncrease = 0.05; // Increase in spawn chance per frame (adjustable)
    private long lastSpawnIncrease = 0;
    
    private int asteroidSpawnSize = 50;
    
    private long startTime = 0; // Time of game start (in nanoseconds)
    private double elapsedTime = 0;
    
    private boolean godMode = false;
    
    private GameOverInterface gameOver = new GameOverInterface();
    
    //Save object
    SaveUserData save;
    
    private Scene view;
    private Pane pane = new Pane();
    
    AnimationTimer gameLoop;
        
    @Override
    public void start (Stage window) {                

        //Layout
        
        pane.setPrefSize(WIDTH, HEIGHT);
        
       
                        
        //Game Objects:
        startTime = System.nanoTime(); // Capture start time
        
        //Timers:
        double shootInterval = 0.5 * this.interval; // 0.5 seconds
        double asteroidSpeedIncrease = this.interval; // 1 second
        
        
        
        
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }
        
        
        
        //Add objects to Pane layout
        pane.getChildren().addAll(ship.getCharacter(), gameOver.getText());
        
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
        //Scene stuff
        view = new Scene(pane);
        
        //Animation:
        
        // key pressed
        view.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });
        // key released
        view.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });
                        
        
        //Animation object
        gameLoop = new AnimationTimer() {
            
            @Override
            public void handle(long now) {
                if (pressedKeys.getOrDefault(KeyCode.A, false)) {
                    ship.turnLeft();
                }
                if (pressedKeys.getOrDefault(KeyCode.D, false)) {
                    ship.turnRight();
                }
                
                if (pressedKeys.getOrDefault(KeyCode.W, false)) {
                    ship.accelerate();
                }
                
                if (pressedKeys.getOrDefault(KeyCode.S, false)) {
                    ship.decelerate();
                }
                
                //Projectiles
                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && now - lastShotTime >= shootInterval) {                    
                    
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(),
                    (int) ship.getCharacter().getTranslateY());
                    //upgrades
                    if (upgradePoints >= 1) {
                        Projectile projectile1 = new Projectile((int) ship.getCharacter().getTranslateX(),
                    (int) ship.getCharacter().getTranslateY());
                        projectileRotation = 30;
                        
                        projectile1.getCharacter().setRotate(ship.getCharacter().getRotate() - projectileRotation);
                        projectiles.add(projectile1);
                        projectile1.accelerate();
                        projectile1.setMovement(projectile1.getMovement().normalize().multiply(3));
                        
                        pane.getChildren().add(projectile1.getCharacter());
                    }
                    
                    if (upgradePoints == 2) {
                        Projectile projectile2 = new Projectile((int) ship.getCharacter().getTranslateX(),
                    (int) ship.getCharacter().getTranslateY());
                        
                    projectile2.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile2);
                    
                    projectile2.accelerate();
                    projectile2.setMovement(projectile2.getMovement().normalize().multiply(3));
                    
                    pane.getChildren().add(projectile2.getCharacter());
                    }
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate() + projectileRotation);
                    projectiles.add(projectile);
                    
                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                    
                    pane.getChildren().add(projectile.getCharacter());
                    lastShotTime = now; // Update last shot time
                }
                
                // Check if it's time to increase asteroid speed
                if (now - lastSpeedIncrease >= asteroidSpeedIncrease) {
                    asteroids.forEach(asteroid -> asteroid.accelerate());
                    lastSpeedIncrease = now;
                }                
                
                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());
                
                //Game time
                elapsedTime = (now - startTime) / interval; // Convert to seconds (optional)
                
                //collision
                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid) & !godMode) {
                        stop();
                        
                        gameOver.resetText();
                        Label time = new Label("Time: " + String.format("%.2f" ,elapsedTime) + "(Sec) Points: " + gameOver.getPoints().intValue());                        
                        
                        //Save user Data
                        save = new SaveUserData(String.format("%.2f" ,elapsedTime),
                                gameOver.getPoints().intValue());
                        save.saveData();
                        
                        
                        //Buttons
                        Button playButton = new Button("play again");
                        Button deleteButton = new Button("Delete all");                        
                        Button exitButton = new Button("Exit");
                        
                        
                        BorderPane borderpane = new BorderPane();
                        borderpane.setPrefSize(WIDTH, HEIGHT /2);
                        borderpane.setPadding(new Insets(20, 20, 20, 20));        
                        gameOver.clearPoints();
                        
                        //boxes
                        HBox hbox = new HBox();
                        hbox.setPadding(new Insets(20, 20, 20, 20));
                        hbox.setAlignment(Pos.CENTER);
                        hbox.setSpacing(10);
                        hbox.getChildren().addAll(playButton, exitButton);
                        
                        VBox vbox = new VBox();
                        vbox.setPadding(new Insets(20, 20, 20, 20));
                        vbox.setAlignment(Pos.CENTER);
                        vbox.setSpacing(10);
                        
                        //Read user Data for display
                        
                        Label preAttempts = new Label("Previous Attempts:");
                        VBox attemptBox = new VBox();                        
                        ScrollPane scrollPane = new ScrollPane(attemptBox);
                        scrollPane.setFitToWidth(true);
                        scrollPane.setPrefHeight(200);
                        //List of previous data
                        List<String> attempts = read.read();                        
                        Set<String> uniqueAttempts = new HashSet<>(attempts); // Ensure no duplicates
                        uniqueAttempts.forEach(attempt -> attemptBox.getChildren().add(new Label(attempt)));
                        
                        vbox.getChildren().addAll(time, hbox);
                        
                        //Left side of borderPane
                        VBox left = new VBox();
                        left.getChildren().addAll(preAttempts, scrollPane, deleteButton);
                        left.setSpacing(5);
                        
                        borderpane.setCenter(vbox);
                        borderpane.setLeft(left);
                        Stage endWindow = new Stage();
                        endWindow.setScene(new Scene(borderpane));
                        endWindow.show();
                        
                        //Button functions
                        playButton.setOnAction(event -> {
                            gameOver();
                            elapsedTime = 0;
                            startTime = System.nanoTime();
                            start();
                            endWindow.close();
                            window.setScene(view);

                        });
                        
                        deleteButton.setOnAction(event -> {
                            attemptBox.getChildren().clear();
                            save.deleteAllData();
                            attempts.clear();
                            
                        });
                        
                        exitButton.setOnAction(event -> {
                            endWindow.close();
                            window.close();
                        });
                    }
                });
                
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if (projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                            gameOver.setText("Points: " + gameOver.getPoints().addAndGet(500) + " Upgrades: " + upgradePoints);
                        }
                    });
                });
                
                if (gameOver.getPoints().intValue() >= 10000 && gameOver.getPoints().intValue() <= 20000) {
                    upgradePoints = 1;
                }
                
                if (gameOver.getPoints().intValue() >= 40000 && gameOver.getPoints().intValue() <= 50000) {
                    upgradePoints = 2;
                }
                
                //Adding asteroids over time
                if (now - lastSpawnIncrease >= interval * 10) {
                   spawnChance = Math.min(spawnChance + spawnIncrease, 1.0);
                   lastSpawnIncrease = now;
                   asteroidSpawnSize += 10;
                }
                
                if (Math.random() < spawnChance) {
                    Asteroid asteroid = new Asteroid (WIDTH, HEIGHT);
                    if (!asteroid.collide(ship) && asteroids.size() < asteroidSpawnSize) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }
                
                //Removing collided objects
                projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));
                
                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .collect(Collectors.toList()));
                
                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
                
                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .collect(Collectors.toList()));
            }
        };
        
        //Start interface
        BorderPane startInterface = new BorderPane();
        Button start = new Button("Start");
        Button deleteButton = new Button("Delete all");      
        Button exitButton = new Button("Exit");
        
        startInterface.setPrefSize(WIDTH, HEIGHT);
        Label asteroids = new Label("ASTEROIDS!!");
        asteroids.setFont(new Font("Times New Roman", 55));
        
        Label preAttempts = new Label("Previous Attempts:");
        preAttempts.setFont(new Font("Arial", 20));
        
        
        VBox vStart = new VBox();        
        vStart.setPadding(new Insets(20, 20, 20, 20));
        vStart.setAlignment(Pos.CENTER);
        vStart.setSpacing(10);                        
        
        
        VBox attemptBox = new VBox();
        ScrollPane scrollPane = new ScrollPane(attemptBox);
        scrollPane.setFitToWidth(true);   
        
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(20, 20, 20, 20));
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10); 
        
        hbox.getChildren().addAll(start, exitButton);
        
        //List of previous data
        List<String> firstAttempts = read.read();
        Set<String> uniqueAttempts = new HashSet<>(firstAttempts); // Ensure no duplicates
        uniqueAttempts.forEach(attempt -> attemptBox.getChildren().add(new Label(attempt)));
        
        
        vStart.getChildren().addAll(asteroids, hbox, preAttempts, scrollPane, deleteButton);
        
        startInterface.setCenter(vStart);        
        
        Scene first = new Scene(startInterface);
        Stage startWindow = new Stage();
        
        start.setOnAction(event -> {
            startWindow.close();
            gameLoop.start();
            window.setTitle("Asteroids!");
            window.setScene(view);
            window.show();
        });
        
        deleteButton.setOnAction(event -> {
                            attemptBox.getChildren().clear();
                            save.deleteAllData();                                                        
                        });
        
        exitButton.setOnAction(event -> {
            startWindow.close();
        });
        
        startWindow.setTitle("Asteroids!");
        startWindow.setScene(first);
        startWindow.show();
    }
    
    public void gameOver () {
        //this.asteroids.stream().forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
        upgradePoints = 0;
        pane.getChildren().clear();
        this.asteroids.clear();
        
        //projectiles.stream().forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));
        
        projectiles.clear();
        
        //apane.getChildren().remove(ship.getCharacter());
        this.ship.resetAcceleration();   
        this.ship = new Ship(WIDTH / 2, HEIGHT / 2);
        this.ship.resetAcceleration();      
        pane.getChildren().addAll(ship.getCharacter(), gameOver.getText());
    }
    
    public static void main(String[] args) {
        Application.launch(AsteroidsApplication.class);
    }    

}
