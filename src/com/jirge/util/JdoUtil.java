package com.jirge.util;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.Collection;

/**
 * TODO: Document me
 *
 * @author Toby Reyelts
 *         Copyright May 19, 2009
 */
public class JdoUtil {
  private static PersistenceManagerFactory pmf =
      JDOHelper.getPersistenceManagerFactory("transactions-optional");

  private static ThreadLocal<PersistenceManager> threadLocalPm =
      new ThreadLocal<PersistenceManager>();

  public static PersistenceManager getPm() {
    PersistenceManager pm = threadLocalPm.get();
    if (pm == null) {
      pm = pmf.getPersistenceManager();
      threadLocalPm.set(pm);
    }
    return pm;
  }

  public static void closePm() {
    PersistenceManager pm = threadLocalPm.get();
    if (pm == null) {
      return;
    }
    if (!pm.isClosed()) {
      pm.close();
    }
    threadLocalPm.set(null);
  }

  public static <T> T queryFirst(Query q, Class<T> klass) {
    Collection c = (Collection)q.execute();
    if (c.size() != 1) {
      throw new RuntimeException("Received " + c.size() + " results instead of 1");
    }
    T result = klass.cast(c.iterator().next());
    q.close(result);
    return result;
  }
}