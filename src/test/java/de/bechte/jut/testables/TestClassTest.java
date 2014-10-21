/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;
import de.bechte.jut.annotations.Context;
import de.bechte.jut.annotations.Test;
import de.bechte.jut.core.TestStatus;
import de.bechte.jut.doubles.core.PositiveTestableFactorySpy;
import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.TestableFactory;
import de.bechte.jut.doubles.core.TestableFactorySpy;
import de.bechte.jut.doubles.samples.TestClassWithMultipleTests;
import de.bechte.jut.doubles.samples.TestClassWithMultipleTestsIgnored;
import de.bechte.jut.doubles.samples.TestClassWithSingleTest;
import de.bechte.jut.doubles.samples.TestClassWithPrivateConstructor;

import static de.bechte.jut.core.ApplicationContext.testableFactory;
import static de.bechte.jut.matchers.ExpectThrowable.expectThrowable;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestClassTest {
  @Test
  public void givenInvalidTestClass_createInstanceFails() throws Exception {
    expectThrowable(AssertionError.class).withMessage("Class not instantiable").in(() -> {
      TestClass<TestClassWithPrivateConstructor> testClass = new TestClass<>(TestClassWithPrivateConstructor.class);
      testClass.createTestInstance();
    });
  }

  @Context
  public class GivenTestableFactorySpy {
    private TestableFactory oldFactory;
    private TestableFactorySpy testableFactorySpy = new PositiveTestableFactorySpy();

    @Before
    public void saveTestableFactorySpyFromApplicationContext() throws Exception {
      oldFactory = testableFactory;
      testableFactory = testableFactorySpy;
    }

    @After
    public void restoreSavedTestableFactoryInApplicationContext() throws Exception {
      testableFactory = oldFactory;
      oldFactory = null;
    }

    private Class<?> classUnderTest;
    private TestClass<?> testClass;

    @Context
    public class GivenTestClassWithSingleTest implements HasValidTestClassBehavior {
      @Before
      public void setupTestClass() throws Exception {
        classUnderTest = TestClassWithSingleTest.class;
        testClass = new TestClass<>(classUnderTest);
      }

      @Override
      public Long getNumberOfTestsInClass() {
        return 1L;
      }

      @Override
      public TestClass getTestClass() {
        return testClass;
      }

      @Override
      public Class getClassUnderTest() {
        return classUnderTest;
      }

      @Override
      public TestableFactorySpy getTestableFactorySpy() {
        return testableFactorySpy;
      }
    }

    @Context
    public class GivenTestClassWithMultipleTests implements HasValidTestClassBehavior {
      @Before
      public void setupTestClass() throws Exception {
        classUnderTest = TestClassWithMultipleTests.class;
        testClass = new TestClass<>(classUnderTest);
      }

      @Override
      public Long getNumberOfTestsInClass() {
        return TestClassWithMultipleTests.NUMBER_OF_TEST_METHODS;
      }

      @Override
      public TestClass getTestClass() {
        return testClass;
      }

      @Override
      public Class getClassUnderTest() {
        return classUnderTest;
      }

      @Override
      public TestableFactorySpy getTestableFactorySpy() {
        return testableFactorySpy;
      }
    }

    @Context
    public class GivenTestClassWithMultipleTestsIgnored implements HasValidTestClassBehavior {
      @Before
      public void setupTestClass() throws Exception {
        classUnderTest = TestClassWithMultipleTestsIgnored.class;
        testClass = new TestClass<>(classUnderTest);
      }

      @Override
      public Long getNumberOfTestsInClass() {
        return TestClassWithMultipleTestsIgnored.NUMBER_OF_TEST_METHODS;
      }

      @Override
      public TestClass getTestClass() {
        return testClass;
      }

      @Override
      public Class getClassUnderTest() {
        return classUnderTest;
      }

      @Override
      public TestableFactorySpy getTestableFactorySpy() {
        return testableFactorySpy;
      }
    }
  }
}

interface HasValidTestClassBehavior {
  @Test
  default void createsAValidTestInstanceOfClassUnderTest() throws Exception {
    Object instance = getTestClass().createTestInstance();
    assertThat(instance, is(instanceOf(getClassUnderTest())));
  }

  @Test
  default void getName_returnsSimpleNameOfClassUnderTest() throws Exception {
    assertThat(getTestClass().getName(), is(getClassUnderTest().getSimpleName()));
  }

  @Test
  default void getUniqueName_returnsCanonicalNameOfClassUnderTest() throws Exception {
    assertThat(getTestClass().getUniqueName(), is(getClassUnderTest().getCanonicalName()));
  }

  @Test
  default void callsRunOnEveryChild() throws Exception {
    getTestClass().run();
    getTestableFactorySpy().allTestables.forEach(t -> assertThat(t.wasRun, is(true)));
  }

  @Test
  default void returnTestResultWithValidResults() throws Exception {
    TestResult testResult = getTestClass().run();
    assertThat(testResult.getStatus(), is(TestStatus.SUCCEEDED));
    assertThat(testResult.getNumberOfTests(), is(getNumberOfTestsInClass()));
    assertThat(testResult.getNumberOfTests(), is(getNumberOfTestsInClass()));
  }

  Long getNumberOfTestsInClass();
  TestClass getTestClass();
  Class getClassUnderTest();
  TestableFactorySpy getTestableFactorySpy();
}