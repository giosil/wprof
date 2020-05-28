package org.dew.wprof.api;

import java.util.Map;

import javax.ejb.Local;

@Local
public 
interface IMonitoring 
{
  /**
   * View monitoring data
   * 
   * @return Map of items with values
   * @throws Exception
   */
  public Map<String, Object> view() throws Exception;
  
  /**
   * Load recorded data
   * 
   * @param filter (es. d="20200528 10")
   * @return Data
   */
  public Map<String, Object> load(Map<String, Object> filter);
  
}
