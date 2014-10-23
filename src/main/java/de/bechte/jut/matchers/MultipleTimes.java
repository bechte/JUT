package de.bechte.jut.matchers;

public class MultipleTimes {
  static final String INVALID_NUMBEROFREPETITIONS = "Number of repetitions must be greater than one!";

  private int numberOfRepetitions = 1;

  public static MultipleTimes multipleTimes(int numberOfRepetitions) {
    if (numberOfRepetitions <= 1)
      throw new IllegalArgumentException(INVALID_NUMBEROFREPETITIONS);
    return new MultipleTimes(numberOfRepetitions);
  }

  private MultipleTimes(int numberOfRepetitions) {
    // Avoid direct instantiation
    this.numberOfRepetitions = numberOfRepetitions;
  }

  public void run(Runnable runnable) {
    for (int i = 0; i < numberOfRepetitions; i++)
      runnable.run();
  }
}