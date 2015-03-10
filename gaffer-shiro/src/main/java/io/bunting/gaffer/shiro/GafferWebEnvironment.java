package io.bunting.gaffer.shiro;

import io.bunting.gaffer.Gaffer;

import org.apache.shiro.ShiroException;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.env.ResourceBasedWebEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Document this class
 */
public class GafferWebEnvironment extends ResourceBasedWebEnvironment implements Initializable, Destroyable
{
	public static final String DEFAULT_WEB_GAFFER_RESOURCE_PATH = "/WEB-INF/shiro.gfr";

	private static final Logger log = LoggerFactory.getLogger(IniWebEnvironment.class);

	private Gaffer gaffer;

	@Override
	public void init() throws ShiroException
	{
		final Gaffer gffr = getGaffer();
//		gffr.loadConfiguredObjects();
	}

	public Gaffer getGaffer()
	{
		return gaffer;
	}

	public void setGaffer(final Gaffer gaffer)
	{
		this.gaffer = gaffer;
	}
}
