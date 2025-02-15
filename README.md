# Asteroids Game  

A simple **Asteroids** game built using **JavaFX**. Control your spaceship, avoid asteroids, and shoot them down to score points! This project is mainly to demonstrate key **Object-Oriented Programming (OOP)** principles such as **inheritance, encapsulation, and polymorphism**.  

## Features  
- Player-controlled spaceship  
- Randomly spawning asteroids  
- Shooting mechanics with projectiles  
- Collision detection and game over logic  
- Score tracking  

## Tech Stack  
- **Java** (JDK 17+)  
- **JavaFX** for graphics and UI  
- **Maven** for dependency management  

## Object-Oriented Programming (OOP) Concepts  

###  Inheritance  
- The `Ship`, `Asteroid`, and `Projectile` classes **inherit** from a common base class that represents game objects.  
- This allows for code reuse and structure, as all game entities share common movement and collision behavior.  

### Encapsulation  
- Each class maintains **private** fields with public getter and setter methods to control access.  
- The `Ship` class encapsulates movement logic, ensuring that external classes cannot directly manipulate its position.  

### Polymorphism  
- The game uses polymorphism to handle different game objects (`Ship`, `Asteroid`, `Projectile`) in a unified way.  
- The `move()` method is overridden in each class to define unique movement behavior for asteroids, the spaceship, and projectiles.  
