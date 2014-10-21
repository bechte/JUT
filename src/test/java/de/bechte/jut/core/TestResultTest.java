/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.doubles.testables.NegativeTestableSpy;
import de.bechte.jut.doubles.testables.PositiveTestableSpy;

import static de.bechte.jut.core.TestStatus.FAILED;
import static de.bechte.jut.core.TestStatus.SKIPPED;
import static de.bechte.jut.core.TestStatus.SUCCEEDED;
import static java.time.Duration.ofMillis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestResultTest {
  private TestResult testResult;

  @Context
  public class GivenOneSuccessfulTestResult {
    @Before
    public void createTestResult() throws Exception {
      testResult = new TestResult(new PositiveTestableSpy(), SUCCEEDED, ofMillis(5));
    }

    @Test
    public void hasNameFromTestable() throws Exception {
      assertThat(testResult.getName(), is(PositiveTestableSpy.NAME));
    }

    @Test
    public void hasUniqueNameFromTestable() throws Exception {
      assertThat(testResult.getUniqueName(), is(PositiveTestableSpy.CANONICAL_NAME));
    }

    @Test
    public void durationIsReturnedCorrectly() throws Exception {
      assertThat(testResult.getDuration(), is(ofMillis(5)));
    }

    @Test
    public void isMarkedSucceeded() throws Exception {
      assertThat(testResult.getStatus(), is(SUCCEEDED));
    }

    @Test
    public void getNumberOfTestsIsOne() throws Exception {
      assertThat(testResult.getNumberOfTests(), is(1L));
    }

    @Test
    public void getNumberOfSuccessfulTestsIsOne() throws Exception {
      assertThat(testResult.getNumberOfSuccessfulTests(), is(1L));
    }

    @Test
    public void getNumberOfFailingTestsIsZero() throws Exception {
      assertThat(testResult.getNumberOfFailingTests(), is(0L));
    }

    @Test
    public void getNumberOfSkippedTestsIsZero() throws Exception {
      assertThat(testResult.getNumberOfSkippedTests(), is(0L));
    }

    @Test
    public void hasNoSubResults() throws Exception {
      assertThat(testResult.getFailures(), is(empty()));
    }

    @Test
    public void hasNoFailure() throws Exception {
      assertThat(testResult.getFailures(), is(empty()));
    }

    @Context
    public class GivenOneSuccessfulEntriesSubTests {
      @Before
      public void addASuccessfulResult() throws Exception {
        testResult.addTestResult(new TestResult(new PositiveTestableSpy(), SUCCEEDED, ofMillis(2)));
      }

      @Test
      public void durationIsCalculatedCorrectly() throws Exception {
        assertThat(testResult.getDuration(), is(equalTo(ofMillis(5))));
      }

      @Test
      public void isMarkedSuccessful() throws Exception {
        assertThat(testResult.getStatus(), is(TestStatus.SUCCEEDED));
      }

      @Test
      public void getNumberOfTestsIsTwo() throws Exception {
        assertThat(testResult.getNumberOfTests(), is(1L));
      }

      @Test
      public void getNumberOfSuccessfulTestsIsTwo() throws Exception {
        assertThat(testResult.getNumberOfSuccessfulTests(), is(1L));
      }

      @Test
      public void getNumberOfFailingTestsIsZero() throws Exception {
        assertThat(testResult.getNumberOfFailingTests(), is(0L));
      }
      
    }
  }

  @Context
  public class GivenOneFailingTestResult {
    AssertionError failure = new AssertionError("FAIL");

    @Before
    public void createTestResult() throws Exception {
      testResult = new TestResult(new NegativeTestableSpy(), FAILED, ofMillis(5));
      testResult.addFailure(failure);
    }

    @Test
    public void isMarkedFailed() throws Exception {
      assertThat(testResult.getStatus(), is(FAILED));
    }

    @Test
    public void getNumberOfTestsIsOne() throws Exception {
      assertThat(testResult.getNumberOfTests(), is(1L));
    }

    @Test
    public void getNumberOfSuccessfulTestsIsZero() throws Exception {
      assertThat(testResult.getNumberOfSuccessfulTests(), is(0L));
    }

    @Test
    public void getNumberOfFailingTestsIsOne() throws Exception {
      assertThat(testResult.getNumberOfFailingTests(), is(1L));
    }

    @Test
    public void getNumberOfSkippedTestsIsZero() throws Exception {
      assertThat(testResult.getNumberOfSkippedTests(), is(0L));
    }

    @Test
    public void returnsTheCorrectFailure() throws Exception {
      assertThat(testResult.getFailures().size(), is(1));
      assertThat(testResult.getFailures(), contains(failure));
    }
  }

  @Context
  public class GivenOneSkippedTestResult {
    @Before
    public void createTestResult() throws Exception {
      testResult = new TestResult(new PositiveTestableSpy(), SKIPPED, ofMillis(1));
    }

    @Test
    public void isMarkedSkipped() throws Exception {
      assertThat(testResult.getStatus(), is(SKIPPED));
    }

    @Test
    public void getNumberOfTestsIsOne() throws Exception {
      assertThat(testResult.getNumberOfTests(), is(1L));
    }

    @Test
    public void getNumberOfSuccessfulTestsIsOne() throws Exception {
      assertThat(testResult.getNumberOfSuccessfulTests(), is(0L));
    }

    @Test
    public void getNumberOfFailingTestsIsZero() throws Exception {
      assertThat(testResult.getNumberOfFailingTests(), is(0L));
    }

    @Test
    public void getNumberOfSkippedTestsIsZero() throws Exception {
      assertThat(testResult.getNumberOfSkippedTests(), is(1L));
    }
  }
}