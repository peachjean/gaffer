package io.bunting.gaffer;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import io.bunting.gaffer.ctx.GafferContext;

import org.codehaus.groovy.control.CompilerConfiguration;

/**
 * TODO: Document this class
 */
public class Gaffer
{
	final GafferConfigSource configSource;

	public Gaffer(final GafferConfigSource configSource)
	{
		this.configSource = configSource;
	}

	public <BK> void loadConfiguredObjects(GafferContext<BK> context) throws IOException
	{
		final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
		compilerConfiguration.setScriptBaseClass(GafferBaseScript.class.getName());
		final Binding binding = new Binding();
		binding.setVariable("thing", new Closure<Object>()
		{

		});
		binding.setProperty("context", context);
		final GroovyShell shell = new GroovyShell(Gaffer.class.getClassLoader(), binding, compilerConfiguration);
		shell.evaluate(configSource.loadReader(), configSource.getName());
	}

	public static Gaffer fromPath(final Path path)
	{
		return new Gaffer(new GafferConfigSource()
		{
			@Override
			public String getName()
			{
				return path.getFileName().toString();
			}

			@Override
			public Reader loadReader() throws IOException
			{
				return Files.newBufferedReader(path, StandardCharsets.UTF_8);
			}
		});
	}
}
