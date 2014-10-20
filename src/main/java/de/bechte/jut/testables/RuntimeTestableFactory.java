/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.testables;

import de.bechte.jut.core.Testable;
import de.bechte.jut.core.TestableFactory;

import java.lang.reflect.Method;

public class RuntimeTestableFactory implements TestableFactory {
  @Override
  public Testable forName(String name) {
    try {
      return forObject(Class.forName(name));
    } catch (Exception e) {
      e.printStackTrace();
    }

    throw new AssertionError(String.format("No Testable found for name <%s>!", name));
  }

  @Override
  public Testable forObject(Object object) {
    if (object instanceof Class)
      return new TestClass((Class) object);

    throw new AssertionError(String.format("No Testable found for object <%s>!", object));
  }

  @Override
  public Testable forChild(Testable parent, Object child) {
    if (parent instanceof TestClass) {
      if (child instanceof Class)
        return new TestContext((TestClass) parent, (Class) child);
      else if (child instanceof Method)
        return new TestMethod((TestClass) parent, (Method) child);
    }

    throw new AssertionError(
        String.format("No Testable found for parent <%s> and child <%s>!", parent.getUniqueName(), child));
  }
}