# Genetic Algorithm

This project provides a modular and extensible Java framework for running genetic algorithms. Below is a description of each class and its role.

---

## Class Descriptions

### `Individu<T>`
- Interface defining a generic individual.
- Requires methods for:
  - Getting and setting value
  - Setting and getting fitness
  - Checking if it's a solution, selected, or a child

### `Generation<T extends Individu<?>>`
- Holds a list of individuals in one generation.
- Tracks generation number and provides:
  - Methods to add, remove, and filter individuals
  - Compute total and selected fitness
  - Identify the best individual

### `Generateur<T extends Individu<?>>`
- Creates a new generation of individuals using a provided supplier.

### `Evaluateur<T, V>`
- Evaluates an individual using a mystery target value.
- Uses a supplier to generate the mystery value and a consumer to evaluate.

### `Selecteur<T extends Individu<?>>`
- Selects individuals based on a provided selection function and a fitness threshold.

### `Entrecroiseur<T extends Individu<?>>`
- Crosses selected individuals using a provided bi-function to create a new generation.

### `Population<T extends Generation<?>>`
- Tracks fitness scores across generations.
- Detects if population fitness is decreasing.

### `Mediateur<T extends Individu<?>, R>`
- Main controller class.
- Coordinates the process: generation, evaluation, selection, crossing, tracking population.
- Uses configuration file `genericConfig.properties` for parameters like:
  - Word length
  - Number of individuals
  - Maximum duration

---

## Requirements

- Java Development Kit (JDK) 8 or later
- Configuration files:
  - `logging.properties`
  - `genericConfig.properties`

---

## Usage

To use this framework:

1. Implement a concrete class:

```java
public class vMotIndividu implements Individu<String> {
    // You need to define value, fitness, isSelected, isSolution, isChild, etc.
}
```

2. Create a `main()` method where you:

- Define `Supplier<T>` for generating random individuals
- Define `BiConsumer<T, R>` for evaluating individuals
- Define `BiFunction<T, T, List<T>>` for creating descendants
- Create an instance of `Mediateur` with the above functions
- Run the `mediateur.run()` method

---

## TODO

-  Implement `vMotIndividu`
-  Create `main()` function to launch the genetic algorithm
