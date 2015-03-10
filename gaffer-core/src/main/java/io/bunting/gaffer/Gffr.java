package io.bunting.gaffer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.bunting.gaffer.ctx.GffrContext;

import org.codehaus.groovy.control.CompilerConfiguration;

/**
 * TODO: Document this class
 */
public class Gffr
{

	public <BK> void loadConfiguredObjects(Path config, GffrContext<BK> context) throws IOException
	{
		final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
		compilerConfiguration.setScriptBaseClass(GffrBaseScript.class.getName());
		final Binding binding = new Binding();
		binding.setProperty("context", context);
		final GroovyShell shell = new GroovyShell(Gffr.class.getClassLoader(), binding, compilerConfiguration);
		shell.evaluate(Files.newBufferedReader(config, StandardCharsets.UTF_8), config.getFileName().toString());
	}
}
