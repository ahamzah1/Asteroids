package asteroids;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.*;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import java.util.concurrent.atomic.AtomicInteger;

public class AsteroidsApplication extends Application {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);

        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
        List<Asteroid> asteroids = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();

        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH - 50), rnd.nextInt(HEIGHT - 50));
            asteroids.add(asteroid);
        }

        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

        Text scoreText = new Text(10, 20, "Points: 0");
        pane.getChildren().add(scoreText);
        
        AtomicInteger points = new AtomicInteger();

        Scene scene = new Scene(pane);
        stage.setTitle("Asteroids!");
        stage.setScene(scene);

        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        scene.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), false));

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 3) {
                    // Shoot projectile
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), 
                                                           (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));

                    pane.getChildren().add(projectile.getCharacter());
                    pressedKeys.put(KeyCode.SPACE, false); // Prevent holding down space for spam
                }

                ship.move();
                asteroids.forEach(Asteroid::move);
                projectiles.forEach(Projectile::move);

                for (Asteroid asteroid : asteroids) {
                    if (ship.collide(asteroid)) {
                        stop();
                        System.out.println("Game Over! Final Score: " + points.get());
                        return;
                    }
                }

                Iterator<Projectile> projectileIterator = projectiles.iterator();
                while (projectileIterator.hasNext()) {
                    Projectile projectile = projectileIterator.next();
                    
                    List<Asteroid> hitAsteroids = asteroids.stream()
                            .filter(asteroid -> asteroid.collide(projectile))
                            .collect(Collectors.toList());

                    if (!hitAsteroids.isEmpty()) {
                        points.addAndGet(hitAsteroids.size() * 100);
                        scoreText.setText("Points: " + points.get());

                        hitAsteroids.forEach(asteroid -> {
                            asteroids.remove(asteroid);
                            pane.getChildren().remove(asteroid.getCharacter());
                        });

                        pane.getChildren().remove(projectile.getCharacter());
                        projectileIterator.remove();
                    }
                }

                if (Math.random() < 0.005) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if (!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }
            }
        }.start();

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
