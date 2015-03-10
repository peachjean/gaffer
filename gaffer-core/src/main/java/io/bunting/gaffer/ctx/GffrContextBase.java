/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package io.bunting.gaffer.ctx;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Provider;

import io.bunting.gaffer.status.BasicStatusManager;
import io.bunting.gaffer.status.StatusManager;
import io.bunting.gaffer.util.CoreConstants;
import io.bunting.gaffer.util.GffrLock;

public abstract class GffrContextBase<BK> implements GffrContext<BK>, GffrLifeCycle {

	private long birthTime = System.currentTimeMillis();

  private String name;
  private StatusManager sm = new BasicStatusManager();
  // TODO propertyMap should be observable so that we can be notified
  // when it changes so that a new instance of propertyMap can be
  // serialized. For the time being, we ignore this shortcoming.
  Map<String, String> propertyMap = new HashMap<String, String>();
	Map<BK, Provider<?>> providerMap = new HashMap<>();

  GffrLock configurationLock = new GffrLock();

  private volatile ExecutorService executorService;
  private GffrLifeCycleManager lifeCycleManager;
  private boolean started;
  
  public StatusManager getStatusManager() {
    return sm;
  }

	@Override
	public void validateBindingType(final Object bindingKey, final Class type)
	{
		// do nothing
	}

	@Override
	public void register(final BK bindingKey, final Provider<?> provider)
	{
		this.providerMap.put(bindingKey, provider);
	}

	@Override
	public <T> Provider<T> access(final BK bindingKey, final Class<T> type)
	{
		return (Provider<T>) this.providerMap.get(bindingKey);
	}

	/**
   * Set the {@link StatusManager} for this context. Note that by default this
   * context is initialized with a {@link BasicStatusManager}. A null value for
   * the 'statusManager' argument is not allowed.
   * <p/>
   * <p> A malicious attacker can set the status manager to a dummy instance,
   * disabling internal error reporting.
   *
   * @param statusManager the new status manager
   */
  public void setStatusManager(StatusManager statusManager) {
    // this method was added in response to http://jira.qos.ch/browse/LBCORE-35
    if (statusManager == null) {
      throw new IllegalArgumentException("null StatusManager not allowed");
    }
    this.sm = statusManager;
  }

  public void putProperty(String key, String val) {
    this.propertyMap.put(key, val);
  }

  /**
   * Given a key, return the corresponding property value. If invoked with
   * the special key "CONTEXT_NAME", the name of the context is returned.
   *
   * @param key
   * @return
   */
  public String getProperty(String key) {
    if (CoreConstants.CONTEXT_NAME_KEY.equals(key))
      return getName();

    return (String) this.propertyMap.get(key);
  }

  public String getName() {
    return name;
  }

  public void start() {
    // We'd like to create the executor service here, but we can't;
    // ContextBase has not always implemented LifeCycle and there are *many*
    // uses (mostly in tests) that would need to be modified.
    started = true;
  }
  
  public void stop() {
    started = false;
  }

  public boolean isStarted() {
    return started;
  }

  /**
   * Clear the internal objectMap and all properties. Removes registered
   * shutdown hook
   */
  public void reset() {
    getLifeCycleManager().reset();
    propertyMap.clear();
	  providerMap.clear();
  }

  /**
   * The context name can be set only if it is not already set, or if the
   * current name is the default context name, namely "default", or if the
   * current name and the old name are the same.
   *
   * @throws IllegalStateException if the context already has a name, other than "default".
   */
  public void setName(String name) throws IllegalStateException {
    if (name != null && name.equals(this.name)) {
      return; // idempotent naming
    }
    if (this.name == null
            || CoreConstants.DEFAULT_CONTEXT_NAME.equals(this.name)) {
      this.name = name;
    } else {
      throw new IllegalStateException("Context has been already given a name");
    }
  }

  public long getBirthTime() {
    return birthTime;
  }

  public void register(GffrLifeCycle component) {
    getLifeCycleManager().register(component);
  }

  /**
   * Gets the life cycle manager for this context.
   * <p>
   * The default implementation lazily initializes an instance of
   * {@link GffrLifeCycleManager}.  Subclasses may override to provide a custom
   * manager implementation, but must take care to return the same manager
   * object for each call to this method.
   * <p>
   * This is exposed primarily to support instrumentation for unit testing.
   * 
   * @return manager object 
   */
  synchronized GffrLifeCycleManager getLifeCycleManager() {
    if (lifeCycleManager == null) {
      lifeCycleManager = new GffrLifeCycleManager();
    }
    return lifeCycleManager;
  }
  
  @Override
  public String toString() {
    return name;
  }

}
