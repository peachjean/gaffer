package io.bunting.gaffer;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;

/**
 * Used by the Gaffer for locating config files.
 */
public interface ConfigReader {
  /**
   * The name returned here will be passed to {@link #readConfig} to load the primary config file.
   *
   * @return the primary config name
   */
  String getName();

  /**
   * This method should load the contents of the config file.
   *
   * @return a reader that contains the groovy contents of the file
   */
  Reader readConfig() throws IOException;

  /**
   * Returns a locator for a config relative to this one. This path will essentially look like a relative file path,
   * where '.' refers to the directory of the current config file.
   * @param relativePath
   * @return a new config locator for the relative path
   */
  ConfigReader relativeLocator(@Nonnull final String relativePath);
}
