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

import io.bunting.gaffer.status.ErrorStatus;
import io.bunting.gaffer.status.Status;
import io.bunting.gaffer.status.StatusManager;
import io.bunting.gaffer.status.WarnStatus;
import io.bunting.gaffer.status.InfoStatus;

/**
 * A helper class that implements ContextAware methods. A class can implement
 * the ContextAware interface by deriving from this class.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class GafferContextAwareBase<BK> implements GafferContextAware<BK> {
  private int noContextWarning = 0;
  protected GafferContext<BK> context;
  final Object declaredOrigin;

  public GafferContextAwareBase() {
    declaredOrigin = this;
  }

  public GafferContextAwareBase(GafferContextAware<BK> declaredOrigin) {
    this.declaredOrigin = declaredOrigin;
  }

  public void setContext(GafferContext<BK> context) {
    if (this.context == null) {
      this.context = context;
    } else if (this.context != context) {
      throw new IllegalStateException("Context has been already set");
    }
  }

  public GafferContext<BK> getContext() {
    return this.context;
  }

  public StatusManager getStatusManager() {
    if (context == null) {
      return null;
    }
    return context.getStatusManager();
  }

  /**
   * The declared origin of status messages. By default 'this'. Derived classes may override this
   * method to declare other origin.
   *
   * @return the declared origin, by default 'this'
   */
  protected Object getDeclaredOrigin() {
    return declaredOrigin;
  }

  public void addStatus(Status status) {
    if (context == null) {
      if (noContextWarning++ == 0) {
        System.out.println("LOGBACK: No context given for " + this);
      }
      return;
    }
    StatusManager sm = context.getStatusManager();
    if (sm != null) {
      sm.add(status);
    }
  }

  public void addInfo(String msg) {
    addStatus(new InfoStatus(msg, getDeclaredOrigin()));
  }

  public void addInfo(String msg, Throwable ex) {
    addStatus(new InfoStatus(msg, getDeclaredOrigin(), ex));
  }

  public void addWarn(String msg) {
    addStatus(new WarnStatus(msg, getDeclaredOrigin()));
  }

  public void addWarn(String msg, Throwable ex) {
    addStatus(new WarnStatus(msg, getDeclaredOrigin(), ex));
  }

  public void addError(String msg) {
    addStatus(new ErrorStatus(msg, getDeclaredOrigin()));
  }

  public void addError(String msg, Throwable ex) {
    addStatus(new ErrorStatus(msg, getDeclaredOrigin(), ex));
  }
}
