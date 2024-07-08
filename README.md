# Asteroids Game

This repository contains the source code for an Asteroids game built using JavaFX. The game is a simple yet fun recreation of the classic arcade game where the player controls a spaceship and must avoid or destroy incoming asteroids.

## Features

- **Smooth and responsive controls**: Control the spaceship using `W`, `A`, `S`, `D` keys for movement and `SPACE` to shoot projectiles.
- **Dynamic difficulty**: Asteroids speed up and spawn more frequently as the game progresses.
- **Upgrade system**: Earn points by destroying asteroids and unlock upgrades for your spaceship.
- **Persistent data**: The game saves and displays previous attempts, including the date, time, and points scored.
- **Game over interface**: Displays points and elapsed time, and allows the player to restart, view previous attempts, or exit the game.

## Gameplay
[![Watch the gameplay video](http://img.youtube.com/vi/pl0HB1B9T8o/0.jpg)](https://youtu.be/pl0HB1B9T8o)

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/your-username/asteroids-game.git
    ```

2. Open the project in your favorite IDE that supports JavaFX.

3. Ensure you have JavaFX properly configured in your development environment.

4. Run the `AsteroidsApplication` class.

## Controls

- `W`: Accelerate
- `A`: Turn left
- `S`: Decelerate
- `D`: Turn right
- `SPACE`: Shoot projectile

## How It Solidifies My Knowledge in Java

This project helped solidify my understanding and skills in the following areas:

- **JavaFX**: Building a graphical user interface and managing different UI components like scenes, panes, and various layout managers.
- **Animation and Game Loops**: Implementing a game loop using `AnimationTimer` to update game objects and handle user input continuously.
- **Object-Oriented Programming (OOP)**: Designing and managing multiple game objects like `Ship`, `Asteroid`, and `Projectile` using inheritance and polymorphism.
- **File I/O**: Reading from and writing to files to save and retrieve user data, allowing persistence across game sessions.
- **Collections Framework**: Utilizing various collections such as `List`, `Map`, and `Set` to manage game objects and user data efficiently.
- **Event Handling**: Handling user input and various game events, such as collisions and game over scenarios.
- **Concurrency**: Managing game state and timing using atomic variables and system time.

## Contributing

Contributions are welcome! If you find any bugs or have suggestions for new features, feel free to open an issue or submit a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

Enjoy playing the game and feel free to explore the code to understand how it works. Happy coding!
