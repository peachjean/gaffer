package io.bunting.gaffer.shiro;

import java.io.IOException;
import java.lang.InstantiationException;

import javax.inject.Provider;

import io.bunting.gaffer.Gaffer;
import io.bunting.gaffer.ctx.GafferContextBase;

import org.apache.shiro.ShiroException;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.util.*;
import org.apache.shiro.web.env.DefaultWebEnvironment;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Document this class
 */
public abstract class GafferWebEnvironment extends DefaultWebEnvironment implements Initializable, Destroyable
{
	private static final Logger log = LoggerFactory.getLogger(GafferWebEnvironment.class);

	private Gaffer gaffer;

	@Override
	public void init() throws ShiroException
	{
		Gaffer gaffer = createGaffer();

		try
		{
			final GafferContextBase<String> context = new GafferContextBase<String>()
			{
				@Override
				public void validateBindingType(final String bindingKey, final Class<?> type)
				{
					// do nothing yet?
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
						throw new org.apache.shiro.util.InstantiationException("Unable to instantiate class [" + type.getName() + "]", e);
					}
				}
			};
			context.register("securityManager", new Provider<WebSecurityManager>()
			{
				@Override
				public WebSecurityManager get()
				{
					return new DefaultWebSecurityManager();
				}
			});
			gaffer.loadConfiguredObjects(context);
		}
		catch (IOException e)
		{
			throw new ConfigurationException("Failed to load shiro configuration.", e);
		}
	}

	protected abstract Gaffer createGaffer();

	public Gaffer getGaffer()
	{
		return gaffer;
	}

	public void setGaffer(final Gaffer gaffer)
	{
		this.gaffer = gaffer;
	}
}
