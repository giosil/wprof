package org.dew.wprof.web;

import java.io.IOException;
import java.io.OutputStream;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dew.wprof.async.AAsyncJob;
import org.dew.wprof.async.AsyncUtils;
import org.dew.wprof.dao.DAOMonitoring;
import org.dew.wprof.util.JSONUtils;
import org.dew.wprof.util.WebUtil;

@WebServlet(name = "WebDataAsync", loadOnStartup = 0, urlPatterns = { "/dataAsync.js" })
public 
class WebDataAsync extends HttpServlet
{
  private static final long serialVersionUID = -1594323916063413764L;
  
  @Override
  protected 
  void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    Map<String, Object> parameters = WebUtil.getParameters(request);
    
    // Create AsyncContext
    AsyncContext asyncContext = AsyncUtils.startAsync(request, response);
    asyncContext.setTimeout(0);
    
    // Create Jobs (Runnable)
    AAsyncJob jobSysData = new AAsyncJob("sys", asyncContext, parameters, true) {
      public Object execute() throws Exception {
        DAOMonitoring daoMonitoring = new DAOMonitoring();
        return daoMonitoring.loadSysData(parameters);
      }
    };
    AAsyncJob jobJvmData = new AAsyncJob("jvm", asyncContext, parameters, true) {
      public Object execute() throws Exception {
        DAOMonitoring daoMonitoring = new DAOMonitoring();
        return daoMonitoring.loadJVMData(parameters);
      }
    };
    AAsyncJob jobEvnData = new AAsyncJob("evn", asyncContext, parameters, true) {
      public Object execute() throws Exception {
        DAOMonitoring daoMonitoring = new DAOMonitoring();
        return daoMonitoring.loadEventsData(parameters);
      }
    };
    
    // Start jobs
    asyncContext.start(jobSysData);
    asyncContext.start(jobJvmData);
    asyncContext.start(jobEvnData);
    
    // Wait for all jobs are completed
    int jobAborted = AsyncUtils.waitForAllJobsAreCompleted(jobSysData, jobJvmData, jobEvnData);
    System.out.println("AsyncUtils.waitForAllJobsAreCompleted -> " + jobAborted);
    
    // Collect data
    Map<String, Object> mapData = new HashMap<String, Object>();
    mapData.put("sys", jobSysData.getOutput());
    mapData.put("jvm", jobJvmData.getOutput());
    mapData.put("evn", jobEvnData.getOutput());
    
    byte[] content = ("var __data=" + JSONUtils.stringify(mapData) + ";").getBytes();
    
    response.setContentType("application/javascript");
    response.setContentLength(content.length);
    
    OutputStream outputStream = response.getOutputStream();
    outputStream.write(content);
    outputStream.flush();
  }
}
