package org.dew.wprof.web;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;

import javax.inject.Inject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dew.wprof.api.IMonitoring;
import org.dew.wprof.util.EventLogger;
import org.dew.wprof.util.TimerTaskRecorder;

@WebServlet(name = "WebIndex", loadOnStartup = 1, urlPatterns = { "/index" })
public 
class WebIndex extends HttpServlet
{
  private static final long serialVersionUID = 5627616166928662309L;
  
  @Inject
  protected IMonitoring monitoring;
  
  protected Timer timer;
  
  public
  void init()
    throws ServletException
  {
    EventLogger.application(this.getClass(), "init");
    
    timer = new Timer();
    
    timer.schedule(new TimerTaskRecorder(),  1000,  60 * 1000);
  }
  
  @Override
  protected 
  void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    Map<String, Object> mapData = null;
    try {
      mapData = monitoring.view();
    }
    catch(Exception ex) {
      request.setAttribute("msg", ex.toString());
    }
    
    request.setAttribute("data", mapData);
    
    RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
    requestDispatcher.forward(request, response);
  }
  
  public
  void destroy()
  {
    EventLogger.application(this.getClass(), "destroy");
    
    if(timer != null) timer.cancel();
  }
}
