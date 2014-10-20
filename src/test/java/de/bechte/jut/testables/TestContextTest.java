/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.samples.*;

import static de.bechte.jut.matchers.ExpectThrowable.expectThrowable;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestContextTest {
  @Context
  public class GivenInvalidTestContext {
    @Test
    public void createInstanceFails() throws Exception {
      expectThrowable(AssertionError.class).withMessage("Class not instantiable").in(() -> {
        TestClass<TestClassWithPrivateContextConstructorDummy> testClass = new TestClass<>(TestClassWithPrivateContextConstructorDummy.class);
        TestContext<TestClassWithPrivateContextConstructorDummy, TestClassWithPrivateContextConstructorDummy.InvalidContext> testContext = new TestContext<>(testClass, TestClassWithPrivateContextConstructorDummy.InvalidContext.class);
        testContext.createTestInstance();
      });
    }
  }

  @Context
  public class WithValidTestContext {
    private SingleTestInContextClassSpy parentInstance;
    private TestClassSpy<SingleTestInContextClassSpy> testClass;
    private TestContext<SingleTestInContextClassSpy, SingleTestInContextClassSpy.TestContext> testContext;

    @Before
    public void setupTestContext() throws Exception {
      parentInstance = new SingleTestInContextClassSpy();
      testClass = new TestClassSpy<>(parentInstance);
      testContext = new TestContext<>(testClass, SingleTestInContextClassSpy.TestContext.class);
    }

    @Test
    public void invokesBeforeMethodsOnParent() throws Exception {
      SingleTestInContextClassSpy.TestContext instance = testContext.createTestInstance();
      testContext.invokeBeforeMethods(instance);
      assertThat(testClass.invocationCounter.getInvocations("invokeBeforeMethods"), is(1));
    }

    @Test
    public void invokesAfterMethodsOnParent() throws Exception {
      SingleTestInContextClassSpy.TestContext instance = testContext.createTestInstance();
      testContext.invokeAfterMethods(instance);
      assertThat(testClass.invocationCounter.getInvocations("invokeAfterMethods"), is(1));
    }

    @Test
    public void returnsTheCorrectParentInstance() throws Exception {
      SingleTestInContextClassSpy.TestContext instance = testContext.createTestInstance();
      SingleTestInContextClassSpy parentInstance = testContext.getParentInstance(instance);
      assertThat(parentInstance, is(this.parentInstance));
    }
  }
}