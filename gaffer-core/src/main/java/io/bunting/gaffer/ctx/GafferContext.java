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

import javax.inject.Provider;

import io.bunting.gaffer.status.StatusManager;

/**
 * A context is the main anchorage point of all logback components.
 *
 * @author Ceki Gulcu
 */
public interface GafferContext<BK> {

  /**
   * Validate that the provided type is valid for the giving binding key. It
   * is perfectly valid for an implementation to do nothing with this method.
   *
   * @param bindingKey
   * @param type
   */
  void validateBindingType(BK bindingKey, Class<?> type);

  /**
   * Registers a provider with a given binding key.
   *
   * @param bindingKey
   * @param provider
   */
  void register(BK bindingKey, Provider<?> provider);

  /**
   * Access the provider that has been registered with a given binding key.
   *
   * @param bindingKey
   * @param type
   * @param <T>
   * @return
   */
  <T> Provider<T> access(BK bindingKey, Class<T> type);

  /**
   * Used by gffr to delegate object instantiation to the configurable component.
   *
   * @param type
   * @param <T>
   * @return
   */
  <T> T instantiate(Class<T> type);

  /**
   * Return the StatusManager instance in use.
   *
   * @return the {@link io.bunting.gaffer.status.StatusManager} instance in use.
   */
  StatusManager getStatusManager();

  /**
   * Get the property of this context.
   */
  String getProperty(String key);

  /**
   * Set a property of this context.
   */
  void putProperty(String key, String value);

  /**
   * Contexts are named objects.
   *
   * @return the name for this context
   */
  String getName();

  /**
   * The name of the context can be set only once.
   *
   * @param name
   */
  void setName(String name);

  /**
   * The time at which this context was created, expressed in
   * millisecond elapsed since the epoch (1.1.1970).
   *
   * @return The time as measured when this class was created.
   */
  long getBirthTime();

  /**
   * Register a component that participates in the context's life cycle.
   * <p/>
   * All components registered via this method will be stopped and removed
   * from the context when the context is reset.
   *
   * @param component the subject component
   */
  void register(GafferLifeCycle component);

}
