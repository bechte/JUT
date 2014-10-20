/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.core;

public interface TestableFactory {
  Testable forName(String name);

  Testable forObject(Object object);

  Testable forChild(Testable parent, Object child);
}