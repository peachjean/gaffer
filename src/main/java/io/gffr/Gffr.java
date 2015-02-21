package io.gffr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Provider;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import org.codehaus.groovy.control.CompilerConfiguration;

/**
 * TODO: Document this class
 */
public class Gffr
{

	public <BK> void loadConfiguredObjects(Path config, RegistrationCallback<BK> callback) throws IOException
	{
		final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
		compilerConfiguration.setScriptBaseClass(GffrBaseScript.class.getName());
		final Binding binding = new Binding();
		binding.setProperty("callback", callback);
		final GroovyShell shell = new GroovyShell(Gffr.class.getClassLoader(), binding, compilerConfiguration);
		shell.evaluate(Files.newBufferedReader(config), config.getFileName().toString());
	}

	public static interface RegistrationCallback<BK>
	{
		void validateBindingType(BK bindingKey, Class<?> type);

		void register(BK bindingKey, Provider<?> provider);

		<T> T instantiate(Class<T> type);
	}


}
