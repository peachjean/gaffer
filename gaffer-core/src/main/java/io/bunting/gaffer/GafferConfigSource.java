package io.bunting.gaffer;

import java.io.IOException;
import java.io.Reader;

/**
 * TODO: Document this class
 */
public interface GafferConfigSource
{
	String getName();

	Reader loadReader() throws IOException;
}
