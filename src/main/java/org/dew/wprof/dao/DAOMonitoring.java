package org.dew.wprof.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public 
class DAOMonitoring 
{
  public static final String FOLDER_PATH = System.getProperty("user.home") + File.separator;
  
  public static final Map<Integer, FInfo> MAP_SYS = new HashMap<Integer, FInfo>();
  static {
    MAP_SYS.put(0, new FInfo("d",   FType.DATE));      // Date time
    MAP_SYS.put(1, new FInfo("cup", FType.DOUBLE));    // CPU usage percentage
    MAP_SYS.put(2, new FInfo("mup", FType.DOUBLE));    // Memory usage percentage
    MAP_SYS.put(3, new FInfo("dup", FType.DOUBLE));    // Disk usage percentage
  }
  
  public static final Map<Integer, FInfo> MAP_JVM = new HashMap<Integer, FInfo>();
  static {
    MAP_JVM.put(0,  new FInfo("d",   FType.DATE));     // Date time
    MAP_JVM.put(1,  new FInfo("msu", FType.INTEGER));  // METASPACE_USED
    MAP_JVM.put(2,  new FInfo("msm", FType.INTEGER));  // METASPACE_MAX
    MAP_JVM.put(3,  new FInfo("heu", FType.INTEGER));  // HEAP_EDEN_USED
    MAP_JVM.put(4,  new FInfo("hem", FType.INTEGER));  // HEAP_EDEN_MAX
    MAP_JVM.put(5,  new FInfo("hsu", FType.INTEGER));  // HEAP_SURVIVOR_USED
    MAP_JVM.put(6,  new FInfo("hsm", FType.INTEGER));  // HEAP_SURVIVOR_MAX
    MAP_JVM.put(7,  new FInfo("htu", FType.INTEGER));  // HEAP_TENURED_USED
    MAP_JVM.put(8,  new FInfo("htm", FType.INTEGER));  // HEAP_TENURED_MAX
    MAP_JVM.put(9,  new FInfo("ccu", FType.INTEGER));  // CODE_CACHE_USED
    MAP_JVM.put(10, new FInfo("ccm", FType.INTEGER));  // CODE_CACHE_MAX
    MAP_JVM.put(11, new FInfo("lcc", FType.INTEGER));  // LOADED_CLASS_COUNT
    MAP_JVM.put(12, new FInfo("tlc", FType.INTEGER));  // TOTAL_LOADED_CLASS_COUNT
    MAP_JVM.put(13, new FInfo("ucc", FType.INTEGER));  // UNLOADED_CLASS_COUNT
    MAP_JVM.put(14, new FInfo("ttc", FType.INTEGER));  // THREAD_COUNT
    MAP_JVM.put(15, new FInfo("ptc", FType.INTEGER));  // PEAK_THREAD_COUNT
    MAP_JVM.put(16, new FInfo("stc", FType.INTEGER));  // TOTAL_STARTED_THREAD_COUNT
  }
  
  public static final Map<Integer, FInfo> MAP_EVN = new HashMap<Integer, FInfo>();
  static {
    MAP_EVN.put(0, new FInfo("d", FType.DATE));        // Date time
    MAP_EVN.put(1, new FInfo("t", FType.STRING));      // EVENT_TYPE
    MAP_EVN.put(2, new FInfo("a", FType.STRING));      // APPLICATION_NAME
    MAP_EVN.put(3, new FInfo("c", FType.STRING));      // CLASS_NAME
    MAP_EVN.put(4, new FInfo("m", FType.STRING));      // METHOD_EVENT
    MAP_EVN.put(5, new FInfo("e", FType.INTEGER));     // ELAPSED
    MAP_EVN.put(6, new FInfo("s", FType.INTEGER));     // RESULT_SIZE
    MAP_EVN.put(7, new FInfo("x", FType.STRING));      // EXCEPTION
  }
  
  public static final Map<String, Map<Integer, FInfo>> MAP_LAYOUTS = new HashMap<String, Map<Integer, FInfo>>();
  static {
    MAP_LAYOUTS.put("sys", MAP_SYS);
    MAP_LAYOUTS.put("jvm", MAP_JVM);
    MAP_LAYOUTS.put("evn", MAP_EVN);
  }

  public 
  List<Map<String, Object>> findSysData(Map<String, Object> filter)
    throws Exception
  {
    return find("wprof_sys.csv", filter);
  }
  
  public 
  List<Map<String, Object>> findJVMData(Map<String, Object> filter)
    throws Exception
  {
    return find("wprof_jvm.csv", filter);
  }
  
  public 
  List<Map<String, Object>> findEventsData(Map<String, Object> filter)
    throws Exception
  {
    return find("wprof_evn.csv", filter);
  }
  
  public 
  List<Map<String, Object>> find(String fileName, Map<String, Object> filter)
    throws Exception
  {
    List<Map<String, Object>> listResult = new ArrayList<Map<String,Object>>();
    
    if(fileName == null || fileName.length() == 0) {
      return listResult;
    }
    
    int ext = fileName.lastIndexOf('.');
    int sep = fileName.lastIndexOf('_');
    if(sep < 0) {
      return listResult;
    }
    String type = ext > sep ? fileName.substring(sep + 1, ext) : fileName.substring(sep + 1);
    
    Map<Integer, FInfo> mapLayout = MAP_LAYOUTS.get(type.toLowerCase());
    if(mapLayout == null || mapLayout.isEmpty()) {
      return listResult;
    }
    
    File file = checkFile(fileName);
    
    if(file == null) return listResult;
    
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(file));
      
      String sLine = null;
      while((sLine = br.readLine()) != null) {
        if(sLine.trim().length() == 0) continue;
        
        Map<String, Object> mapRow = parseRow(sLine, mapLayout);
        if(mapRow == null || mapRow.isEmpty()) continue;
        
        if(match(mapRow, filter)) {
          listResult.add(mapRow);
        }
      }
    }
    finally {
      if(br != null) try{ br.close(); } catch(Exception ex) {}
    }
    
    return listResult;
  }
  
  @SuppressWarnings("unchecked")
  protected static
  boolean match(Map<String, Object> mapRow, Map<String, Object> filter)
  {
    if(filter == null || filter.isEmpty()) {
      return true;
    }
    if(mapRow == null) {
      return false;
    }
    
    Iterator<Map.Entry<String, Object>> iterator = filter.entrySet().iterator();
    while(iterator.hasNext()) {
      Map.Entry<String, Object> entry = iterator.next();
      
      String sKey = entry.getKey();
      
      Object val  = entry.getValue();
      String sVal = val == null ? "null" : val.toString();
      
      boolean boStartsWithPerc = false;
      boolean boEndsWithPerc   = false;
      boStartsWithPerc = sKey.startsWith("%");
      if(boStartsWithPerc) sKey = sKey.substring(1);
      boEndsWithPerc = sKey.endsWith("%");
      if(boEndsWithPerc) sKey = sKey.substring(0, sKey.length()-1);
      
      boolean boGTE  = sKey.startsWith(">=");
      boolean boLTE  = sKey.startsWith("<=");
      boolean boNE   = sKey.startsWith("<>");
      if(!boNE) boNE = sKey.startsWith("!=");
      if(boGTE || boLTE || boNE) {
        sKey = sKey.substring(2);
      }
      else {
        boGTE  = sKey.endsWith(">=");
        boLTE  = sKey.endsWith("<=");
        boNE   = sKey.endsWith("<>");
        if(!boNE) boNE = sKey.endsWith("!=");
        if(boGTE || boLTE || boNE) {
          sKey = sKey.substring(0, sKey.length()-2);
        }
      }
      
      boolean boGT  = sKey.startsWith(">");
      boolean boLT  = sKey.startsWith("<");
      if(boGT || boLT) {
        sKey = sKey.substring(1);
      }
      else {
        boGT = sKey.endsWith(">");
        boLT = sKey.endsWith("<");
        if(boGT || boLT) {
          sKey = sKey.substring(0, sKey.length()-1);
        }
      }
      
      Object ivl = mapRow.get(sKey);
      int cmp = 0;
      if(ivl == null && val == null) {
        cmp = 0; // equals
      }
      else if(ivl == null && val != null) {
        cmp = 1;
      }
      else if(ivl != null && val == null) {
        cmp = -1;
      }
      else if(ivl instanceof Comparable && val instanceof Comparable) {
        cmp = ((Comparable<Object>) ivl).compareTo(val);
      }
      
      if(sVal != null && !(boGTE || boLTE || boNE || boGT || boLT)) {
        boGTE  = sVal.startsWith(">=");
        boLTE  = sVal.startsWith("<=");
        boNE   = sVal.startsWith("<>");
        if(!boNE) boNE = sVal.startsWith("!=");
        if(boGTE || boLTE || boNE) sVal = sVal.substring(2);
        
        boGT   = sVal.startsWith(">");
        boLT   = sVal.startsWith("<");
        if(boGT || boLT) sVal = sVal.substring(1);
      }
      
      if(sVal.startsWith("%")) {
        sVal = sVal.substring(1);
        boStartsWithPerc = true;
      }
      if(sVal.endsWith("%")) {
        sVal = sVal.substring(0, sVal.length()-1);
        boEndsWithPerc = true;
      }
      
      if(sVal.equals("null")) {
        if(boNE) {
          if(ivl == null) return false;
        }
        else {
          if(ivl != null) return false;
        }
        continue;
      }
      
      if(boNE) {
        if(val == null && ivl == null)     return false;
        if(val != null && val.equals(ivl)) return false;
      }
      else if(boGT) {
        if(cmp < 1) return false;
      }
      else if(boLT) {
        if(cmp > -1) return false;
      }
      else if(boGTE) {
        if(cmp == -1) return false;
      }
      else if(boLTE) {
        if(cmp == 1) return false;
      }
      else {
        if(boStartsWithPerc || boEndsWithPerc) {
          String sIvl = ivl == null ? "null" : ivl.toString();
          if(boStartsWithPerc && boEndsWithPerc) {
            if(sIvl.indexOf(sIvl) < 0) return false;
          }
          else if(boStartsWithPerc) {
            if(!sIvl.endsWith(sVal)) return false;
          }
          else if(boEndsWithPerc) {
            if(!sIvl.startsWith(sVal)) return false;
          }
        }
        else {
          if(cmp != 0) return false;
        }
      }
    }
    return true;
  }
  
  protected
  Map<String,Object> parseRow(String row, Map<Integer, FInfo> mapLayout)
  {
    if(row == null) {
      return new HashMap<String, Object>();
    }
    row = row.trim();
    if(row.length() == 0) {
      return new HashMap<String, Object>();
    }
    
    List<String> list = new ArrayList<String>();
    int iIndexOf = 0;
    int iBegin   = 0;
    iIndexOf = row.indexOf(';');
    while(iIndexOf >= 0) {
      list.add(row.substring(iBegin, iIndexOf));
      iBegin = iIndexOf + 1;
      iIndexOf = row.indexOf(';', iBegin);
    }
    list.add(row.substring(iBegin));
    
    Map<String, Object> mapResult = new HashMap<String, Object>(mapLayout.size());
    
    for(int i = 0; i < list.size(); i++) {
      FInfo  finfo = mapLayout.get(i);
      if(finfo == null) break;
      
      String value = list.get(i);
      FType  type = finfo.getType();
      switch (type) {
      case INTEGER:
        try {
          mapResult.put(finfo.name, Integer.parseInt(extractNumber(value, true)));
        }
        catch(Exception ex) {
          mapResult.put(finfo.name, 0);
        }
        
        break;
      case DOUBLE:
        try {
          mapResult.put(finfo.name, Double.parseDouble(extractNumber(value, false)));
        }
        catch(Exception ex) {
          mapResult.put(finfo.name, 0.0d);
        }
        
        break;
      case BOOLEAN:
        mapResult.put(finfo.name, toBoolean(value));
        
        break;
      case DATE:
        mapResult.put(finfo.name, value);
        
        break;
      
      default:
        mapResult.put(finfo.name, value);
        
        break;
      }
    }
    
    return mapResult;
  }
  
  protected static boolean toBoolean(String value) {
    if(value == null || value.length() == 0) {
      return false;
    }
    char c0 = value.charAt(0);
    return "1TYSJtysj".indexOf(c0) >= 0;
  }
  
  protected static String extractNumber(String value, boolean expectedInteger) {
    if(value == null || value.length() == 0) {
      return "0";
    }
    value = value.trim();
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      if(c == '-') {
        if(sb.length() != 0) break;
        sb.append(c);
      }
      else if(c == '.' || c == ',') {
        if(expectedInteger) break;
        sb.append('.');
      }
      else if(Character.isDigit(c)) {
        sb.append(c);
      }
    }
    if(sb.length() == 0) sb.append('0');
    return sb.toString();
  }
  
  protected
  File checkFile(String fileName)
  {
    File file = new File(FOLDER_PATH + fileName);
    
    if(file.exists()) {
      return file;
    }
    
    return null;
  }
  
  protected static 
  enum FType
  {
    STRING, INTEGER, DOUBLE, BOOLEAN, DATE;
  }
  
  protected static 
  class FInfo
  {
    private String name;
    private FType  type;
    
    public FInfo()
    {
    }
    
    public FInfo(String name, FType type)
    {
      this.name = name;
      this.type = type;
    }
    
    public String getName() {
      return name;
    }
    
    public void setName(String name) {
      this.name = name;
    }
    
    public FType getType() {
      return type;
    }
    
    public void setType(FType type) {
      this.type = type;
    }
    
    @Override
    public boolean equals(Object object) {
      if(object instanceof FInfo) {
        String sName = ((FInfo) object).getName();
        if(name == null) return sName == null;
        return name.equals(sName);
      }
      return false;
    }
    
    @Override
    public int hashCode() {
      if(name == null) return 0;
      return name.hashCode();
    }
    
    @Override
    public String toString() {
      return name;
    }
  }
}
