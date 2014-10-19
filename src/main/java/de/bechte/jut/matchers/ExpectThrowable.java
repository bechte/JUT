/*
 * Copyright (c) 2014. Stefan Bechtold. All rights reserved.
 */

package de.bechte.jut.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.CombinableMatcher;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public final class ExpectThrowable {
  public static ExpectThrowable expectThrowable(Class<? extends Throwable> throwable) {
    return new ExpectThrowable(throwable);
  }

  public static ExpectThrowable expectThrowable(Matcher<? super Throwable> throwableMatcher) {
    return new ExpectThrowable(throwableMatcher);
  }

  private CombinableMatcher<Throwable> throwableMatcher;

  private ExpectThrowable(Class<? extends Throwable> throwable) {
    this(is(instanceOf(throwable)));
  }

  private ExpectThrowable(Matcher<? super Throwable> throwableMatcher) {
    this.throwableMatcher = new CombinableMatcher<>(throwableMatcher);
  }

  public ExpectThrowable withMessage(String message) {
    withMessage(containsString(message));
    return this;
  }

  public ExpectThrowable withMessage(Matcher<String> matcher) {
    this.throwableMatcher.and(new TypeSafeMatcher<Throwable>() {
      @Override
      protected boolean matchesSafely(Throwable item) {
        return matcher.matches(item.getMessage());
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("exception with message ");
        description.appendDescriptionOf(matcher);
      }
    });
    return this;
  }

  public void in(Runnable runnable) {
    try {
      runnable.run();
    } catch (Throwable t) {
      assertThat(t, throwableMatcher);
      return;
    }

    throw new AssertionError("Expected test to throw " + StringDescription.asString(throwableMatcher));
  }
}