package io.gffr;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import io.gffr.Gffr.RegistrationCallback;

/**
 * TODO: Document this class
 */
class SimpleContext implements RegistrationCallback<String>
{
	private final Map<String, Provider<?>> bindings = new HashMap<>();

	@Override
	public void validateBindingType(final String bindingKey, final Class<?> type)
	{
		// do nothing
	}

	@Override
	public void register(final String bindingKey, final Provider<?> provider)
	{
		this.bindings.put(bindingKey, provider);
	}

	@Override
	public <T> T instantiate(final Class<T> type)
	{
		try
		{
			return type.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException("Failed to instantiate " + type, e);
		}
	}

	public <T> T loadBindings(String key, Class<T> expectedType)
	{
		return expectedType.cast(this.bindings.get(key).get());
	}
}
