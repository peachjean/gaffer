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

import java.util.HashSet;
import java.util.Set;

/**
 * An object that manages a collection of components that implement the
 * {@link GafferLifeCycle} interface.  Each component that is added to the manager
 * will be stopped and removed from the manager when the manager is reset.
 *
 * @author Carl Harris
 */
public class GafferLifeCycleManager {

  private final Set<GafferLifeCycle> components = new HashSet<GafferLifeCycle>();

  /**
   * Registers a component with this manager.
   * <p/>
   *
   * @param component the component whose life cycle is to be managed
   */
  public void register(GafferLifeCycle component) {
    components.add(component);
  }

  /**
   * Resets this manager.
   * <p/>
   * All registered components are stopped and removed from the manager.
   */
  public void reset() {
    for (GafferLifeCycle component : components) {
      if (component.isStarted()) {
        component.stop();
      }
    }
    components.clear();
  }

}
