package io.bunting.gaffer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.bunting.gaffer.ctx.GafferContext;

import org.codehaus.groovy.control.CompilerConfiguration;

/**
 * TODO: Document this class
 */
public class Gaffer
{
	final Path configLocation;

	public Gaffer(final Path configLocation)
	{
		this.configLocation = configLocation;
	}

	public <BK> void loadConfiguredObjects(GafferContext<BK> context) throws IOException
	{
		final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
		compilerConfiguration.setScriptBaseClass(GafferBaseScript.class.getName());
		final Binding binding = new Binding();
		binding.setProperty("context", context);
		final GroovyShell shell = new GroovyShell(Gaffer.class.getClassLoader(), binding, compilerConfiguration);
		shell.evaluate(Files.newBufferedReader(configLocation, StandardCharsets.UTF_8), configLocation.getFileName().toString());
	}
}
