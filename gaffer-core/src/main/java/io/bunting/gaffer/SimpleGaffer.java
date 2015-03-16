package io.bunting.gaffer;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import javax.inject.Provider;
import java.beans.*;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a configuration setup that will load groovy config files from the config reader and will provide them
 * in the form of an implementation of the provided GOAL interface. This gaffer provides the groovy scripts with a few
 * methods: {@code bind} and {@code ref}. The {@code bind} method takes a name, a type, and a closure. The type will be
 * instantiated using a no-arg constructor, the closure will be called with the new object as its delegate, and then the
 * resulting object will be stored in a registry map with the key 'name'.
 *
 * The GOAL interface must consist of solely no-arg getter methods. The "name" of the getter will map to a binding with
 * the same name, of the same or a derivative type.
 */
public class SimpleGaffer<GOAL> implements Gaffer {

  private final ConfigReader configReader;
  private final Class<GOAL> goalClass;

  public SimpleGaffer(final ConfigReader configReader, final Class<GOAL> goalClass) {
    this.configReader = configReader;
    this.goalClass = goalClass;
  }

  GOAL load() {
    final Map<String, Provider<?>> ctx = new HashMap<>();

    evaluteConfigScript(ctx);

    return goalClass.cast(Proxy.newProxyInstance(goalClass.getClassLoader(), new Class<?>[]{goalClass}, new InvocationHandler() {
      final Map<String, String> methodToBeanMap = new HashMap<String, String>();

      {
        try {
          final BeanInfo beanInfo = Introspector.getBeanInfo(goalClass);
          for (PropertyDescriptor propertyDescriptor: beanInfo.getPropertyDescriptors())
          {
            methodToBeanMap.put(propertyDescriptor.getReadMethod().getName(), propertyDescriptor.getName());
          }
        } catch (IntrospectionException e) {
          throw new GafferException("Could not resolve bean info for goal class %s.", goalClass);
        }
      }

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass().equals(Object.class))
        {
          return method.invoke(proxy, args);
        }
        final String beanName = methodToBeanMap.get(method.getName());

        if (beanName == null)
        {
          throw new GafferException("The gaffer context is unable to fulfill goal method %s.", method.getName());
        }

        final Provider<?> provider = ctx.get(beanName);
        return provider == null ? null : provider.get();
      }
    }));
  }

  private void evaluteConfigScript(Map<String, Provider<?>> ctx) {
    final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
    compilerConfiguration.setScriptBaseClass(GafferBaseScript.class.getName());
    final Binding binding = new Binding();
    binding.setProperty("context", ctx);
    final GroovyShell shell = new GroovyShell(Gaffer.class.getClassLoader(), binding, compilerConfiguration);
    try {
      shell.evaluate(configReader.readConfig(), configReader.getName());
    } catch (IOException e) {
      throw new GafferException("Failed to evaluate gaffer config file %s.", e, configReader);
    }
  }
}
