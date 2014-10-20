/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.doubles.samples;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;

public class TestContextWithConflictingSetupAndTeardowns {
  public enum Priority {
    INNER, OUTER
  }

  public Priority setupPriority;
  public Priority teardownPriority;

  @Before
  public void setUp() throws Exception {
    TestContextWithConflictingSetupAndTeardowns.this.setupPriority = Priority.OUTER;
  }

  @After
  public void tearDown() throws Exception {
    TestContextWithConflictingSetupAndTeardowns.this.teardownPriority = Priority.OUTER;
  }

  @Context
  public class ConflictingContext {
    @Before
    public void setUp() throws Exception {
      TestContextWithConflictingSetupAndTeardowns.this.setupPriority = Priority.INNER;
    }

    @After
    public void tearDown() throws Exception {
      TestContextWithConflictingSetupAndTeardowns.this.teardownPriority = Priority.INNER;
    }

    @Test
    public void testMethod() {
    }

    public Priority getSetupPriority() {
      return TestContextWithConflictingSetupAndTeardowns.this.setupPriority;
    }

    public Priority getTeardownPriority() {
      return TestContextWithConflictingSetupAndTeardowns.this.teardownPriority;
    }
  }
}