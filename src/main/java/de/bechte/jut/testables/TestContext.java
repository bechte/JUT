/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.annotations.After;
import de.bechte.jut.annotations.Before;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestContext<P, T> extends TestClass<T> {
  private TestClass<P> parent;

  public TestContext(TestClass<P> parent, Class<T> classUnderTest) {
    super(classUnderTest);
    this.parent = parent;
  }

  @Override
  public T createTestInstance() {
    try {
      P parentInstance = parent.createTestInstance();
      Constructor<T> innerConstructor = classUnderTest.getConstructor(parentInstance.getClass());
      return innerConstructor.newInstance(parentInstance);
    } catch (Throwable t) {
      throw new AssertionError("Class not instantiable: " + classUnderTest.getTypeName(), t);
    }
  }

  @Override
  public void invokeBeforeMethods(T testInstance) throws InvocationTargetException, IllegalAccessException {
    parent.invokeBeforeMethods(getParentInstance(testInstance));
    invokeMethodsForAnnotation(testInstance, Before.class);
  }

  @Override
  public void invokeAfterMethods(T testInstance) throws InvocationTargetException, IllegalAccessException {
    invokeMethodsForAnnotation(testInstance, After.class);
    parent.invokeAfterMethods(getParentInstance(testInstance));
  }

  protected P getParentInstance(T testInstance) throws IllegalAccessException {
    List<Field> fields = Stream.of(classUnderTest.getDeclaredFields())
        .filter(f -> f.isSynthetic())
        .filter(f -> f.getType().equals(parent.classUnderTest))
        .collect(Collectors.toList());

    if (!fields.isEmpty()) {
      fields.get(0).setAccessible(true);
      return (P) fields.get(0).get(testInstance);
    }

    throw new IllegalStateException("Member instance has no field containing the enclosing instance!");
  }
}