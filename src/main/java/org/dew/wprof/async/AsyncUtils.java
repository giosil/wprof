package org.dew.wprof.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public 
class AsyncUtils 
{
  public static int DEFAULT_JOB_TIMEOUT_MILLIS = 180000;
  
  public static
  AsyncContext startAsync(ServletRequest request, ServletResponse response)
  {
    if(request == null) {
      return new SimpleAsyncContext();
    }
    if(!request.isAsyncSupported()) {
      System.out.println("request.isAsyncSupported() -> false");
      return new SimpleAsyncContext(request, response);
    }
    try {
      return request.startAsync();
    }
    catch(Exception ex) {
      System.err.println("request.startAsync() -> " + ex);
    }
    return new SimpleAsyncContext(request, response);
  }
  
  public static 
  int waitForAllJobsAreCompleted(AAsyncJob<?>... jobs) 
  {
    return waitForAllJobsAreCompleted(250, DEFAULT_JOB_TIMEOUT_MILLIS, jobs);
  }
  
  public static 
  int waitForAllJobsAreCompleted(int timeoutMillis, AAsyncJob<?>... jobs) 
  {
    return waitForAllJobsAreCompleted(250, timeoutMillis, jobs);
  }
  
  public static 
  int waitForAllJobsAreCompleted(int checkEveryMillis, int timeoutMillis, AAsyncJob<?>... jobs) 
  {
    // The result is count of job completed with error.
    int result = 0;
    int elasped = 0;
    boolean allCompleted = false;
    while(!allCompleted){
      try {
        Thread.sleep(checkEveryMillis);
        elasped += checkEveryMillis;
      }
      catch(Exception ignore) {
      }
      
      result = 0;
      allCompleted = true;
      for(int i = 0; i < jobs.length; i++) {
        AAsyncJob<?> job = jobs[i];
        if(job == null) continue;
        if(job.isCompleted()) {
          result += job.isSuccess() ? 0 : 1;
        }
        else {
          allCompleted = false;
        }
      }
      
      if(timeoutMillis > 0 && elasped > timeoutMillis) {
        break;
      }
    }
    return result;
  }
}
