/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.doubles.testables.TestClassMock;
import de.bechte.jut.doubles.samples.TestContextWithConflictingSetupAndTeardowns;
import de.bechte.jut.doubles.samples.TestContextWithPrivateConstructor;
import de.bechte.jut.doubles.samples.TestContextWithSingleTest;

import static de.bechte.jut.matchers.ExpectThrowable.expectThrowable;
import static de.bechte.jut.doubles.samples.TestContextWithConflictingSetupAndTeardowns.Priority;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestContextTest {
  @Test
  public void givenInvalidTestContext_createInstanceFails() throws Exception {
    expectThrowable(AssertionError.class).withMessage("Class not instantiable").in(() -> {
      TestClass<TestContextWithPrivateConstructor> testClass = new TestClass<>(TestContextWithPrivateConstructor.class);
      TestContext<TestContextWithPrivateConstructor, TestContextWithPrivateConstructor.InvalidContext> testContext =
          new TestContext<>(testClass, TestContextWithPrivateConstructor.InvalidContext.class);
      testContext.createTestInstance();
    });
  }

  @Context
  public class GivenTestContextWithSingleTest {
    private TestContextWithSingleTest parentInstance;
    private TestClassMock<TestContextWithSingleTest> testClass;
    private TestContext<TestContextWithSingleTest, TestContextWithSingleTest.TestContext> testContext;

    @Before
    public void setupTestContext() throws Exception {
      parentInstance = new TestContextWithSingleTest();
      testClass = new TestClassMock<>(parentInstance);
      testContext = new TestContext<>(testClass, TestContextWithSingleTest.TestContext.class);
    }

    @Test
    public void createsAValidTestInstanceOfClassUnderTest() throws Exception {
      TestContextWithSingleTest.TestContext instance = testContext.createTestInstance();
      assertThat(instance, is(instanceOf(TestContextWithSingleTest.TestContext.class)));
    }

    @Test
    public void invokesSetupTestInstanceOnParent() throws Exception {
      TestContextWithSingleTest.TestContext instance = testContext.createTestInstance();
      testContext.setupTestInstance(instance);
      assertThat(testClass.testInstanceOnSetup, is(parentInstance));
    }

    @Test
    public void invokesTeardownTestInstanceOnParent() throws Exception {
      TestContextWithSingleTest.TestContext instance = testContext.createTestInstance();
      testContext.teardownTestInstance(instance);
      assertThat(testClass.testInstanceOnTeardown, is(parentInstance));
    }

    @Test
    public void returnsTheCorrectParentInstance() throws Exception {
      TestContextWithSingleTest.TestContext instance = testContext.createTestInstance();
      assertThat(testContext.lookupParentInstance(instance), is(parentInstance));
    }
  }

  @Context
  public class GivenTestContextWithConflictingSetupAndTeardowns {
    private TestContextWithConflictingSetupAndTeardowns parentInstance;
    private TestClass<TestContextWithConflictingSetupAndTeardowns> testClass;
    private TestContext<TestContextWithConflictingSetupAndTeardowns, TestContextWithConflictingSetupAndTeardowns.ConflictingContext> testContext;

    @Before
    public void setupTestContext() throws Exception {
      parentInstance = new TestContextWithConflictingSetupAndTeardowns();
      testClass = new TestClass<>(TestContextWithConflictingSetupAndTeardowns.class);
      testContext = new TestContext<>(testClass, TestContextWithConflictingSetupAndTeardowns.ConflictingContext.class);
    }

    @Test
    public void invokesSetupTestInstanceOnParentBeforeItSetupsTheTestsInstanceItself() throws Exception {
      TestContextWithConflictingSetupAndTeardowns.ConflictingContext instance = testContext.createTestInstance();
      testContext.setupTestInstance(instance);
      assertThat(instance.getSetupPriority(), is(Priority.INNER));
    }

    @Test
    public void invokesTeardownTestInstanceOnParentAfterItTeardownsTheTestsInstanceItself() throws Exception {
      TestContextWithConflictingSetupAndTeardowns.ConflictingContext instance = testContext.createTestInstance();
      testContext.teardownTestInstance(instance);
      assertThat(instance.getTeardownPriority(), is(Priority.OUTER));
    }
  }
}