/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.reporting;

import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.doubles.testables.PositiveTestableSpy;

import java.time.Duration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestResultEntryTest {
  @Context
  public class GivenASuccessfulTestResultEntry {
    TestResultEntry testResultEntry;

    @Before
    public void createTestResultEntry() throws Exception {
      testResultEntry = new TestResultEntry(new PositiveTestableSpy());
      testResultEntry.success();
    }

    @Test
    public void hasNameFromTestable() throws Exception {
      assertThat(testResultEntry.getName(), is(PositiveTestableSpy.NAME));
    }

    @Test
    public void hasUniqueNameFromTestable() throws Exception {
      assertThat(testResultEntry.getUniqueName(), is(PositiveTestableSpy.CANONICAL_NAME));
    }

    @Test
    public void durationIsCalcuatedCorrectly() throws Exception {
      assertThat(testResultEntry.getDuration(), is(Duration.between(testResultEntry.testStarted, testResultEntry.testEnded)));
    }

    @Test
    public void isMarkedSuccessful() throws Exception {
      assertThat(testResultEntry.isSuccessful(), is(true));
    }

    @Test
    public void getNumberOfTestsIsOne() throws Exception {
      assertThat(testResultEntry.getNumberOfTests(), is(1L));
    }

    @Test
    public void getNumberOfSuccessfulTestsIsOne() throws Exception {
      assertThat(testResultEntry.getNumberOfSuccessfulTests(), is(1L));
    }

    @Test
    public void getNumberOfFailingTestsIsZero() throws Exception {
      assertThat(testResultEntry.getNumberOfFailingTests(), is(0L));
    }

    @Test
    public void hasNoFailure() throws Exception {
      assertThat(testResultEntry.getFailure(), is(nullValue()));
    }
  }

  @Context
  public class GivenAFailingTestResultEntry {
    AssertionError failure = new AssertionError("FAIL");
    TestResultEntry testResultEntry;

    @Before
    public void createTestResultEntry() throws Exception {
      testResultEntry = new TestResultEntry(new PositiveTestableSpy());
      testResultEntry.fail(failure);
    }

    @Test
    public void isNotMarkedSuccessful() throws Exception {
      assertThat(testResultEntry.isSuccessful(), is(false));
    }


    @Test
    public void getNumberOfTestsIsOne() throws Exception {
      assertThat(testResultEntry.getNumberOfTests(), is(1L));
    }

    @Test
    public void getNumberOfSuccessfulTestsIsZero() throws Exception {
      assertThat(testResultEntry.getNumberOfSuccessfulTests(), is(0L));
    }

    @Test
    public void getNumberOfFailingTestsIsOne() throws Exception {
      assertThat(testResultEntry.getNumberOfFailingTests(), is(1L));
    }

    @Test
    public void returnsTheCorrectFailure() throws Exception {
      assertThat(testResultEntry.getFailure(), is(failure));
    }
  }
}
