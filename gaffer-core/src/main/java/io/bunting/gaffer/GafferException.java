package io.bunting.gaffer;

import java.nio.file.Path;
import java.util.Objects;

/**
 * The base exception thrown by gaffer components.
 */
public class GafferException extends RuntimeException {
  public GafferException(final String message, final Object ... args) {
    super(String.format(message, args));
  }

  public GafferException(final String message, final Throwable cause, final Object ... args) {
    super(String.format(message, args), cause);
  }
}
