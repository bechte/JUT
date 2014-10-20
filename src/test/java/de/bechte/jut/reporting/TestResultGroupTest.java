/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.reporting;

import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.doubles.reporting.TestResultEntryStub;
import de.bechte.jut.doubles.testables.PositiveTestableSpy;

import java.time.Duration;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestResultGroupTest {
  private TestResultGroup testResultGroup = new TestResultGroup(new PositiveTestableSpy());

  @Test
  public void returnsNameFromTestable() throws Exception {
    assertThat(testResultGroup.getName(), is(PositiveTestableSpy.NAME));
  }

  @Test
  public void returnsUniqueNameFromTestable() throws Exception {
    assertThat(testResultGroup.getUniqueName(), is(PositiveTestableSpy.CANONICAL_NAME));
  }

  @Context
  public class GivenOneSuccessfulEntry {
    @Before
    public void addSuccessfulEntry() throws Exception {
      testResultGroup.addTestResults(Collections.singleton(new TestResultEntryStub("Entry1", true, 1L)));
    }

    @Test
    public void durationIsCalculatedCorrectly() throws Exception {
      assertThat(testResultGroup.getDuration(), is(Duration.ofMillis(1L)));
    }

    @Test
    public void isMarkedSuccessful() throws Exception {
      assertThat(testResultGroup.isSuccessful(), is(true));
    }

    @Test
    public void getNumberOfTestsIsOne() throws Exception {
      assertThat(testResultGroup.getNumberOfTests(), is(1L));
    }

    @Test
    public void getNumberOfSuccessfulTestsIsOne() throws Exception {
      assertThat(testResultGroup.getNumberOfSuccessfulTests(), is(1L));
    }

    @Test
    public void getNumberOfFailingTestsIsZero() throws Exception {
      assertThat(testResultGroup.getNumberOfFailingTests(), is(0L));
    }

    @Context
    public class GivenTwoSuccessfulEntries {
      @Before
      public void addAnotherSuccessfulEntry() throws Exception {
        testResultGroup.addTestResults(Collections.singleton(new TestResultEntryStub("Entry2", true, 2L)));
      }

      @Test
      public void durationIsCalculatedCorrectly() throws Exception {
        assertThat(testResultGroup.getDuration(), is(Duration.ofMillis(3L)));
      }

      @Test
      public void isMarkedSuccessful() throws Exception {
        assertThat(testResultGroup.isSuccessful(), is(true));
      }

      @Test
      public void getNumberOfTestsIsTwo() throws Exception {
        assertThat(testResultGroup.getNumberOfTests(), is(2L));
      }

      @Test
      public void getNumberOfSuccessfulTestsIsTwo() throws Exception {
        assertThat(testResultGroup.getNumberOfSuccessfulTests(), is(2L));
      }

      @Test
      public void getNumberOfFailingTestsIsZero() throws Exception {
        assertThat(testResultGroup.getNumberOfFailingTests(), is(0L));
      }
    }

    @Context
    public class GivenOneSuccessfulAndOneFailingEntry {
      @Before
      public void addFailingEntry() throws Exception {
        testResultGroup.addTestResults(Collections.singleton(new TestResultEntryStub("Entry2", false, 2L)));
      }

      @Test
      public void isNotMarkedSuccessful() throws Exception {
        assertThat(testResultGroup.isSuccessful(), is(false));
      }

      @Test
      public void getNumberOfTestsIsTwo() throws Exception {
        assertThat(testResultGroup.getNumberOfTests(), is(2L));
      }

      @Test
      public void getNumberOfSuccessfulTestsIsOne() throws Exception {
        assertThat(testResultGroup.getNumberOfSuccessfulTests(), is(1L));
      }

      @Test
      public void getNumberOfFailingTestsIsOne() throws Exception {
        assertThat(testResultGroup.getNumberOfFailingTests(), is(1L));
      }

    }
  }

  @Context
  public class GivenOneFailingEntry {
    @Before
    public void addFailingEntry() throws Exception {
      testResultGroup.addTestResults(Collections.singleton(new TestResultEntryStub("Entry1", false, 1L)));
    }

    @Test
    public void isNotMarkedSuccessful() throws Exception {
      assertThat(testResultGroup.isSuccessful(), is(false));
    }

    @Test
    public void getNumberOfTestsIsOne() throws Exception {
      assertThat(testResultGroup.getNumberOfTests(), is(1L));
    }

    @Test
    public void getNumberOfSuccessfulTestsIsZero() throws Exception {
      assertThat(testResultGroup.getNumberOfSuccessfulTests(), is(0L));
    }

    @Test
    public void getNumberOfFailingTestsIsOne() throws Exception {
      assertThat(testResultGroup.getNumberOfFailingTests(), is(1L));
    }
  }
}
