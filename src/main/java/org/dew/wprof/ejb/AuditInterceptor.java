package org.dew.wprof.ejb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import javax.ejb.EJBContext;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.dew.wprof.util.EventLogger;

public 
class AuditInterceptor 
{
  @Resource
  protected EJBContext ejbContext;
  
  @AroundInvoke
  public 
  Object intercept(InvocationContext invocationContext) 
    throws Exception 
  {
    Object target      = invocationContext.getTarget();
    String targetClass = target != null ? target.getClass().getName() : "-";
    String methodName  = invocationContext.getMethod().getName();
    
    EventLogger.entering(targetClass, methodName);
    
    long startTime = System.currentTimeMillis();
    try {
      Object result = invocationContext.proceed();
      
      EventLogger.exiting(targetClass, methodName, startTime, System.currentTimeMillis(), getSize(result));
      
      return result;
    }
    catch(Exception ex) {
      EventLogger.exiting(targetClass, methodName, startTime, System.currentTimeMillis(), ex);
      throw ex;
    }
  }
  
  public static 
  int getSize(Object object)
  {
    int result = 0;
    if(object == null) {
      return result;
    }
    else if(object instanceof Collection) {
      result = ((Collection<?>) object).size();
      if(result > 0) {
        Iterator<?> iterator = ((Collection<?>) object).iterator();
        if(iterator.hasNext()) {
          int iSizeItem0 = getSize(iterator.hasNext());
          if(iSizeItem0 < 1) iSizeItem0 = 1;
          result = result * iSizeItem0;
        }
      }
    }
    else if(object instanceof Map) {
      result = ((Map<?, ?>) object).size() * 2;
    }
    else {
      result = 1;
    }
    return result;
  }
}
