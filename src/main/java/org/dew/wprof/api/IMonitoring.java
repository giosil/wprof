package org.dew.wprof.api;

import java.util.Map;

import javax.ejb.Local;

@Local
public 
interface IMonitoring 
{
  
  public Map<String, Object> view() throws Exception;
  
  public Map<String, Object> load(Map<String, Object> filter);
  
}
