package org.dew.wprof.web;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Map;

import javax.inject.Inject;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dew.wprof.api.IMonitoring;

import org.dew.wprof.util.JSONUtils;
import org.dew.wprof.util.WebUtil;

@WebServlet(name = "WebData", loadOnStartup = 0, urlPatterns = { "/data.js" })
public 
class WebData extends HttpServlet
{
  private static final long serialVersionUID = -1594323916063413764L;
  
  @Inject
  protected IMonitoring monitoring;
  
  @Override
  protected 
  void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
    Map<String, Object> mapData = monitoring.load(WebUtil.getParameters(request));
    
    byte[] content = ("var __data=" + JSONUtils.stringify(mapData) + ";").getBytes();
    
    response.setContentType("application/javascript");
    response.setContentLength(content.length);
    
    OutputStream outputStream = response.getOutputStream();
    outputStream.write(content);
    outputStream.flush();
  }
}
