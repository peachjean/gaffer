package io.bunting.gaffer.shiro;

import io.bunting.gaffer.Gffr;

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

	private Gffr gaffer;

	@Override
	public void init() throws ShiroException
	{
		final Gffr gffr = getGaffer();
//		gffr.loadConfiguredObjects();
	}

	public Gffr getGaffer()
	{
		return gaffer;
	}

	public void setGaffer(final Gffr gaffer)
	{
		this.gaffer = gaffer;
	}
}
