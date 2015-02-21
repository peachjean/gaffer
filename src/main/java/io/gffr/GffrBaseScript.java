package io.gffr;

import javax.inject.Provider;

import groovy.lang.Closure;
import groovy.lang.Script;
import io.gffr.Gffr.RegistrationCallback;

/**
 * TODO: Document this class
 */
public abstract class GffrBaseScript extends Script
{
	void bind(Object bindingKey, final Class<?> type, final Closure closure)
	{
		final RegistrationCallback callback = (RegistrationCallback) this.getBinding().getProperty("callback");
		callback.register(bindingKey, new Provider<Object>()
		{
			@Override
			public Object get()
			{
				final Object obj = callback.instantiate(type);
				final Closure dehydrate = closure.dehydrate();
				dehydrate.setResolveStrategy(Closure.DELEGATE_FIRST);
				dehydrate.setDelegate(obj);
				dehydrate.call();
				return obj;
			}
		});
	}
}
