package io.gffr;

import javax.inject.Provider;

import groovy.lang.Closure;
import groovy.lang.Script;
import io.gffr.ctx.GffrContext;

/**
 * TODO: Document this class
 */
public abstract class GffrBaseScript extends Script
{
	void bind(Object bindingKey, final Class<?> type, final Closure closure)
	{
		final GffrContext context = (GffrContext) this.getBinding().getProperty("context");
		context.register(bindingKey, new Provider<Object>()
		{
			@Override
			public Object get()
			{
				final Object obj = context.instantiate(type);
				final Closure dehydrate = closure.dehydrate();
				dehydrate.setResolveStrategy(Closure.DELEGATE_FIRST);
				final BindingDelegate delegate = new BindingDelegate(obj);
				delegate.setContext(context);
				dehydrate.setDelegate(delegate);
				dehydrate.call();
				return obj;
			}
		});
	}

	Object ref(Object bindingKey)
	{
		final GffrContext context = (GffrContext) this.getBinding().getProperty("context");
		return context.access(bindingKey, Object.class).get();
	}
}
