/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.matchers;

import de.bechte.jut.annotations.Test;

import static de.bechte.jut.matchers.ExpectThrowable.expectThrowable;
import static de.bechte.jut.matchers.MultipleTimes.multipleTimes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MultipleTimesTest {
  @Test
  public void givenAPositiveNumberOfRepetitions_performsNumberOfRepetitionRuns() throws Exception {
    int repetitions = 10;
    int[] counter = { 0 };
    multipleTimes(repetitions).run(() -> { counter[0]++; });
    assertThat(counter[0], is(repetitions));
  }

  @Test
  public void givenOnlyOneRepetition_throwsIllegalArgumentException() throws Exception {
    expectThrowable(IllegalArgumentException.class)
        .withMessage(MultipleTimes.INVALID_NUMBEROFREPETITIONS).in(() -> {
      multipleTimes(1).run(() -> {});
    });
  }

  @Test
  public void givenZeroRepetitions_throwsIllegalArgumentException() throws Exception {
    expectThrowable(IllegalArgumentException.class)
        .withMessage(MultipleTimes.INVALID_NUMBEROFREPETITIONS).in(() -> {
      multipleTimes(0).run(() -> {});
    });
  }

  @Test
  public void givenNegativeNumberOfRepetitions_throwsIllegalArgumentException() throws Exception {
    expectThrowable(IllegalArgumentException.class)
        .withMessage(MultipleTimes.INVALID_NUMBEROFREPETITIONS).in(() -> {
      multipleTimes(-1).run(() -> {});
    });
  }
}