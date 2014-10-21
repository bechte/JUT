/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.annotations.*;
import de.bechte.jut.core.TestResult;
import de.bechte.jut.core.TestStatus;
import de.bechte.jut.core.TestableGroup;
import de.bechte.jut.core.Testable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.bechte.jut.core.ApplicationContext.testableFactory;
import static java.util.stream.Collectors.*;

public class TestClass<T> implements TestableGroup<T> {
  protected Class<T> classUnderTest;

  public TestClass(Class<T> classUnderTest) {
    this.classUnderTest = classUnderTest;
  }

  @Override
  public String getName() {
    return classUnderTest.getSimpleName();
  }

  @Override
  public String getUniqueName() {
    return classUnderTest.getCanonicalName();
  }

  @Override
  public TestResult run() {
    if (classUnderTest.isAnnotationPresent(Ignore.class))
      return new TestResult(this, TestStatus.SKIPPED, Duration.ZERO);

    LocalDateTime start = LocalDateTime.now();
    Collection<TestResult> testResults = getTestResults();
    Duration duration = Duration.between(start, LocalDateTime.now());
    Collection<Throwable> failures = getFailures(testResults);

    if (failures.isEmpty()) {
      TestResult testResult = new TestResult(this, TestStatus.SUCCEEDED, duration);
      testResult.addTestResults(testResults);
      return testResult;
    } else {
      TestResult testResult = new TestResult(this, TestStatus.FAILED, duration);
      testResult.addTestResults(testResults);
      testResult.addFailures(failures);
      return testResult;
    }
  }

  private Collection<TestResult> getTestResults() {
    return getTests().stream()
          .map(t -> t.run())
          .collect(Collectors.toList());
  }

  private Collection<Throwable> getFailures(Collection<TestResult> testResults) {
    return testResults.stream()
        .map(r -> r.getFailures())
        .reduce(new LinkedList<>(), (l, r) -> { l.addAll(r); return l; });
  }

  protected Collection<? extends Testable> getTests() {
    Collection<Testable> tests = new LinkedList<>();
    tests.addAll(getTestMethods());
    tests.addAll(getTestContexts());
    return tests;
  }

  protected Collection<? extends Testable> getTestContexts() {
    return Stream.of(classUnderTest.getClasses())
        .filter(c -> c.isAnnotationPresent(Context.class))
        .map(c -> testableFactory.forChild(TestClass.this, c))
        .collect(toList());
  }

  protected Collection<? extends Testable> getTestMethods() {
    return getMethodsForAnnotation(Test.class)
        .map(m -> testableFactory.forChild(TestClass.this, m))
        .collect(toList());
  }

  protected Stream<Method> getMethodsForAnnotation(Class<? extends Annotation> annotation) {
    return Stream.of(classUnderTest.getMethods())
        .filter(c -> c.isAnnotationPresent(annotation));
  }

  @Override
  public T createTestInstance() {
    try {
      return classUnderTest.newInstance();
    } catch (Throwable t) {
      throw new AssertionError("Class not instantiable: " + classUnderTest.getTypeName(), t);
    }
  }

  @Override
  public void setupTestInstance(T testInstance) {
    invokeMethodsForAnnotation(testInstance, Before.class);
  }

  @Override
  public void teardownTestInstance(T testInstance) {
    invokeMethodsForAnnotation(testInstance, After.class);
  }

  protected void invokeMethodsForAnnotation(T testInstance, Class<? extends Annotation> annotation) {
    try {
      Collection<Method> annotatedMethods = getMethodsForAnnotation(annotation).collect(toList());
      for (Method m : annotatedMethods)
        m.invoke(testInstance);
    } catch (IllegalAccessException e) {
      throw new AssertionError(e.getMessage(), e);
    } catch (InvocationTargetException e) {
      throw new AssertionError(e.getTargetException().getMessage(), e.getTargetException());
    }
  }
}