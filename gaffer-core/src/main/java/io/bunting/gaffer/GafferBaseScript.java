package io.bunting.gaffer;

import groovy.lang.Closure;
import groovy.lang.Script;

import javax.inject.Provider;
import java.util.Map;

/**
 * TODO: Document this class
 */
public abstract class GafferBaseScript extends Script {
  void bind(String bindingKey, final Class<?> type, final Closure closure)
  {
    final Map<String, Provider<?>> context = getContext();
    final Provider<Object> provider = createProvider(context, type, closure);
    context.put(bindingKey, provider);
  }

  private Map<String, Provider<?>> getContext() {
    return (Map<String, Provider<?>>) this.getBinding().getProperty("context");
  }

  static Provider<Object> createProvider(final Map<String, Provider<?>> context, final Class<?> type, final Closure closure)
  {
    return new Provider<Object>()
    {
      @Override
      public Object get()
      {
        final Object obj = instantiate(type);
        final Closure dehydrate = closure.dehydrate();
        dehydrate.setResolveStrategy(Closure.DELEGATE_FIRST);
        final BindingDelegate delegate = new BindingDelegate(obj, context);
        dehydrate.setDelegate(delegate);
        dehydrate.call();
        return obj;
      }
    };
  }

  private static Object instantiate(Class<?> type) {
    try {
      return type.newInstance();
    } catch (InstantiationException e) {
      throw new GafferException("Failed to instantiate %s.", e, type);
    } catch (IllegalAccessException e) {
      throw new GafferException("Failed to instantiate %s.", e, type);
    }
  }

  Object ref(String bindingKey)
  {

    final Map<String, Provider<?>> context = getContext();
    return context.get(bindingKey).get();
  }
}
