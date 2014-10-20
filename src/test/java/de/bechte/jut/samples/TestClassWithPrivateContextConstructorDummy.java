/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.samples;

import de.bechte.jut.annotations.Context;

public class TestClassWithPrivateContextConstructorDummy {
  @Context
  public class InvalidContext {
    private InvalidContext() {
      // Do not allow instantiation
    }
  }
}