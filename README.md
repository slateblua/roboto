# roboto

A graphical 2D simulation for inverse kinematics (IK) numerical solutions, built with [libGDX](https://libgdx.com/).

## Overview

`roboto` is a simulation and visualization environment for inverse kinematics solutions. 

## Features

- **2D Visualization**: Real-time rendering of robot arm movements and targets.
- **Scene2D UI**: Built-in user interface components for interaction and parameter adjustment.
- **Cross-Platform**: Built on libGDX, supporting multiple platforms (currently configured for desktop via LWJGL3).

### Running the Application

To run the simulation on your desktop, use the following Gradle command:

```bash
./gradlew lwjgl3:run
```

## Development

### Implementing a New Solver

To create a custom IK solver, implement the `Solver` interface:

```java
public class MyCustomSolver implements Solver {
    @Override
    public Array<Angle> solve(final Tuple target) {
        // Your IK numerical solution logic here
        return new Array<Angle>();
    }
}
```

### Building a Runnable JAR

To create a standalone executable JAR for the desktop version:

```bash
./gradlew lwjgl3:jar
```
The JAR will be located in `lwjgl3/build/libs`.

## Acknowledgments

- This project was generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).
