package io.bunting.gaffer;

import org.apache.commons.lang3.SystemUtils;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A config reader that reads from simple Java paths.
 */
public class PathConfigReader implements ConfigReader {

  private final Path path;

  public PathConfigReader(@Nonnull final Path path) {
    if (!Files.isRegularFile(path))
    {
      throw new GafferException("The provided config path [%s] is not a valid file.", path.toAbsolutePath());
    }
    if (!Files.isReadable(path))
    {
      throw new GafferException("The current user [%s] cannot read provided config path [%s].", System.getProperty("user.name"), path.toAbsolutePath());
    }
    this.path = path.toAbsolutePath();
  }

  @Override
  public String getName() {
    return path.getFileName().toString();
  }

  @Override
  public Reader readConfig() throws IOException {
    return Files.newBufferedReader(path, StandardCharsets.UTF_8);
  }

  @Override
  public ConfigReader relativeLocator(@Nonnull String relativePath) {
    final Path resolvedPath = path.resolveSibling(relativePath);
    return new PathConfigReader(resolvedPath);
  }

  @Override
  public String toString() {
    return "ConfigReader[" + path + ']';
  }
}
