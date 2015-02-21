package io.gffr;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import io.gffr.ctx.GffrContextBase;

/**
 * TODO: Document this class
 */
class SimpleContext extends GffrContextBase<String>
{
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
		return expectedType.cast(this.access(key, expectedType).get());
	}


}
