package io.bunting.gaffer;

import io.bunting.gaffer.ctx.GafferContextBase;

/**
 * TODO: Document this class
 */
class SimpleContext extends GafferContextBase<String>
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
