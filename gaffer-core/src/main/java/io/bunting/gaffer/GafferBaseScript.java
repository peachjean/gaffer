package io.bunting.gaffer;

import javax.inject.Provider;

import groovy.lang.Closure;
import groovy.lang.Script;
import io.bunting.gaffer.ctx.GafferContext;

/**
 * TODO: Document this class
 */
public abstract class GafferBaseScript extends Script {
  void bind(Object bindingKey, final Class<?> type, final Closure closure) {
    final GafferContext context = (GafferContext) this.getBinding().getProperty("context");
    final Provider<Object> provider = createProvider(context, type, closure);
    context.register(bindingKey, provider);
  }

  static Provider<Object> createProvider(final GafferContext context, final Class<?> type, final Closure closure) {
    return new Provider<Object>() {
      @Override
      public Object get() {
        final Object obj = context.instantiate(type);
        final Closure dehydrate = closure.dehydrate();
        dehydrate.setResolveStrategy(Closure.DELEGATE_FIRST);
        final BindingDelegate delegate = new BindingDelegate(obj);
        delegate.setContext(context);
        dehydrate.setDelegate(delegate);
        dehydrate.call();
        return obj;
      }
    };
  }

  Object ref(Object bindingKey) {
    final GafferContext context = (GafferContext) this.getBinding().getProperty("context");
    return context.access(bindingKey, Object.class).get();
  }
}
