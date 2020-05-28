 package org.dew.wprof.ejb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.toMap;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.dew.wprof.api.IMonitoring;
import org.dew.wprof.dao.DAOMonitoring;

import java.lang.management.*;

@Stateless
@Local(IMonitoring.class)
@Interceptors(AuditInterceptor.class)
public 
class WMonitoring implements IMonitoring 
{
  @Override
  public 
  Map<String, Object> view() 
    throws Exception
  {
    Map<String, Object> mapResult = new TreeMap<String, Object>();
    
    // Memory Usage
    
    List<MemoryPoolMXBean> list = ManagementFactory.getMemoryPoolMXBeans();
    
    Map<String, Object> mapMemory = list.stream().collect(toMap(MemoryPoolMXBean::getName, m -> m.getUsage().toString()));
    
    mapResult.putAll(mapMemory);
    
    // Class loading
    
    ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
    
    mapResult.put("Loaded Class Count",         classLoadingMXBean.getLoadedClassCount());
    mapResult.put("Total Loaded Class Count",   classLoadingMXBean.getTotalLoadedClassCount());
    mapResult.put("Unloaded Class Count",       classLoadingMXBean.getUnloadedClassCount());
    
    // Threads
    
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    
    mapResult.put("Thread Count",               threadMXBean.getThreadCount());
    mapResult.put("Peak Thread Count",          threadMXBean.getPeakThreadCount());
    mapResult.put("Total Started Thread Count", threadMXBean.getTotalStartedThreadCount());
    
    // Runtime
    
    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    
    mapResult.put("Name Run Time", runtimeMXBean.getName());
    
    return mapResult;
  }
  
  @Override
  public 
  Map<String, Object> load(Map<String, Object> filter) 
  {
    Map<String, Object> mapResult = new HashMap<String, Object>();
    
    try {
      DAOMonitoring daoMonitoring = new DAOMonitoring();
      
      mapResult.put("sys", daoMonitoring.loadSysData(filter));
      mapResult.put("jvm", daoMonitoring.loadJVMData(filter));
      mapResult.put("evn", daoMonitoring.loadEventsData(filter));
      
      mapResult.putAll(daoMonitoring.layouts());
    }
    catch(Exception ex) {
      ex.printStackTrace();
      mapResult.put("msg", ex.toString());
    }
    
    return mapResult;
  }
  
}
