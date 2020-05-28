package org.dew.wprof.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public 
class DAOMonitoring 
{
  public static final String FOLDER_PATH = System.getProperty("user.home") + File.separator;
  
  public static final Map<Integer, FInfo> MAP_SYS = new HashMap<Integer, FInfo>();
  static {
    MAP_SYS.put(0, new FInfo("d",   FType.DATE));      //  0 Date time
    MAP_SYS.put(1, new FInfo("cup", FType.DOUBLE));    //  1 CPU usage percentage
    MAP_SYS.put(2, new FInfo("mup", FType.DOUBLE));    //  2 Memory usage percentage
    MAP_SYS.put(3, new FInfo("dup", FType.DOUBLE));    //  3 Disk usage percentage
  }
  
  public static final Map<Integer, FInfo> MAP_JVM = new HashMap<Integer, FInfo>();
  static {
    MAP_JVM.put(0,  new FInfo("d",   FType.DATE));     //  0 Date time
    MAP_JVM.put(1,  new FInfo("msu", FType.INTEGER));  //  1 METASPACE_USED
    MAP_JVM.put(2,  new FInfo("msm", FType.INTEGER));  //  2 METASPACE_MAX
    MAP_JVM.put(3,  new FInfo("heu", FType.INTEGER));  //  3 HEAP_EDEN_USED
    MAP_JVM.put(4,  new FInfo("hem", FType.INTEGER));  //  4 HEAP_EDEN_MAX
    MAP_JVM.put(5,  new FInfo("hsu", FType.INTEGER));  //  5 HEAP_SURVIVOR_USED
    MAP_JVM.put(6,  new FInfo("hsm", FType.INTEGER));  //  6 HEAP_SURVIVOR_MAX
    MAP_JVM.put(7,  new FInfo("htu", FType.INTEGER));  //  7 HEAP_TENURED_USED
    MAP_JVM.put(8,  new FInfo("htm", FType.INTEGER));  //  8 HEAP_TENURED_MAX
    MAP_JVM.put(9,  new FInfo("ccu", FType.INTEGER));  //  9 CODE_CACHE_USED
    MAP_JVM.put(10, new FInfo("ccm", FType.INTEGER));  // 10 CODE_CACHE_MAX
    MAP_JVM.put(11, new FInfo("lcc", FType.INTEGER));  // 11 LOADED_CLASS_COUNT
    MAP_JVM.put(12, new FInfo("tlc", FType.INTEGER));  // 12 TOTAL_LOADED_CLASS_COUNT
    MAP_JVM.put(13, new FInfo("ucc", FType.INTEGER));  // 13 UNLOADED_CLASS_COUNT
    MAP_JVM.put(14, new FInfo("ttc", FType.INTEGER));  // 14 THREAD_COUNT
    MAP_JVM.put(15, new FInfo("ptc", FType.INTEGER));  // 15 PEAK_THREAD_COUNT
    MAP_JVM.put(16, new FInfo("stc", FType.INTEGER));  // 16 TOTAL_STARTED_THREAD_COUNT
  }
  
  public static final Map<Integer, FInfo> MAP_EVN = new HashMap<Integer, FInfo>();
  static {
    MAP_EVN.put(0, new FInfo("d", FType.DATE));        //  0 Date time
    MAP_EVN.put(1, new FInfo("t", FType.STRING));      //  1 EVENT_TYPE
    MAP_EVN.put(2, new FInfo("a", FType.STRING));      //  2 APPLICATION_NAME
    MAP_EVN.put(3, new FInfo("c", FType.STRING));      //  3 CLASS_NAME
    MAP_EVN.put(4, new FInfo("m", FType.STRING));      //  4 METHOD_EVENT
    MAP_EVN.put(5, new FInfo("e", FType.INTEGER));     //  5 ELAPSED
    MAP_EVN.put(6, new FInfo("s", FType.INTEGER));     //  6 RESULT_SIZE
    MAP_EVN.put(7, new FInfo("x", FType.STRING));      //  7 EXCEPTION
  }
  
  public static final Map<String, Map<Integer, FInfo>> MAP_LAYOUTS = new HashMap<String, Map<Integer, FInfo>>();
  static {
    MAP_LAYOUTS.put("sys", MAP_SYS);
    MAP_LAYOUTS.put("jvm", MAP_JVM);
    MAP_LAYOUTS.put("evn", MAP_EVN);
  }
  
  public 
  List<List<Object>> loadSysData(Map<String, Object> filter)
    throws Exception
  {
    return load("wprof_sys.csv", filter);
  }
  
  public 
  List<List<Object>> loadJVMData(Map<String, Object> filter)
    throws Exception
  {
    return load("wprof_jvm.csv", filter);
  }
  
  public 
  List<List<Object>> loadEventsData(Map<String, Object> filter)
    throws Exception
  {
    return load("wprof_evn.csv", filter);
  }
  
  public 
  Map<String, Object> layouts()
    throws Exception
  {
    Map<String,Object> mapResult = new HashMap<String, Object>();
    
    List<String> listSysFields = new ArrayList<String>();
    for(int i = 0; i < MAP_SYS.size(); i++) {
      FInfo finfo = MAP_SYS.get(i);
      listSysFields.add(finfo != null ? finfo.name : String.valueOf(i));
    }
    mapResult.put("sys_fields", listSysFields);
    
    List<String> listJvmFields = new ArrayList<String>();
    for(int i = 0; i < MAP_JVM.size(); i++) {
      FInfo finfo = MAP_JVM.get(i);
      listJvmFields.add(finfo != null ? finfo.name : String.valueOf(i));
    }
    mapResult.put("jvm_fields", listJvmFields);
    
    List<String> listEvnFields = new ArrayList<String>();
    for(int i = 0; i < MAP_EVN.size(); i++) {
      FInfo finfo = MAP_EVN.get(i);
      listEvnFields.add(finfo != null ? finfo.name : String.valueOf(i));
    }
    mapResult.put("evn_fields", listEvnFields);
    
    return mapResult;
  }
  
  public 
  List<List<Object>> load(String fileName, Map<String, Object> filter)
    throws Exception
  {
    List<List<Object>> listResult = new ArrayList<List<Object>>();
    
    if(fileName == null || fileName.length() == 0) {
      return listResult;
    }
    
    Object  filterDateTime = filter.get("d");
    String sFilterDateTime = filterDateTime != null ? filterDateTime.toString() : null;
    
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
        
        List<Object> listRow = parseRow(sLine, mapLayout);
        if(listRow == null || listRow.isEmpty()) continue;
        
        if(sFilterDateTime != null && sFilterDateTime.length() > 0) {
          Object  dateTime = listRow.get(0);
          String sDateTime = dateTime != null ? dateTime.toString() : "";
          if(!sDateTime.startsWith(sFilterDateTime)) {
            continue;
          }
        }
        
        listResult.add(listRow);
      }
    }
    finally {
      if(br != null) try{ br.close(); } catch(Exception ex) {}
    }
    
    return listResult;
  }
  
  protected
  List<Object> parseRow(String row, Map<Integer, FInfo> mapLayout)
  {
    if(row == null) {
      return new ArrayList<Object>();
    }
    row = row.trim();
    if(row.length() == 0) {
      return new ArrayList<Object>();
    }
    
    List<Object> listResult = new ArrayList<Object>(mapLayout.size());
    
    int iIndexOf = 0;
    int iBegin   = 0;
    iIndexOf = row.indexOf(';');
    while(iIndexOf >= 0) {
      listResult.add(row.substring(iBegin, iIndexOf));
      iBegin = iIndexOf + 1;
      iIndexOf = row.indexOf(';', iBegin);
    }
    listResult.add(row.substring(iBegin));
    
    for(int i = 0; i < listResult.size(); i++) {
      FInfo  finfo = mapLayout.get(i);
      if(finfo == null) break;
      
      Object value = listResult.get(i);
      FType  type = finfo.getType();
      switch (type) {
      case INTEGER:
        try {
          listResult.set(i, Integer.parseInt(extractNumber(value, true)));
        }
        catch(Exception ex) {
          listResult.set(i, 0);
        }
        
        break;
      case DOUBLE:
        try {
          listResult.set(i, Double.parseDouble(extractNumber(value, false)));
        }
        catch(Exception ex) {
          listResult.set(i, 0.0d);
        }
        
        break;
      case BOOLEAN:
        listResult.set(i, toBoolean(value));
        
        break;
      default:
        
        break;
      }
    }
    return listResult;
  }
  
  protected static boolean toBoolean(Object value) {
    if(value == null) return false;
    if(value instanceof Boolean) {
      return ((Boolean) value).booleanValue();
    }
    if(value instanceof Number) {
      return ((Number) value).intValue() != 0;
    }
    if(value instanceof String) {
      String sValue = (String) value;
      if(sValue.length() == 0) return false;
      char c0 = sValue.charAt(0);
      return "1TYSJtysj".indexOf(c0) >= 0;
    }
    return false;
  }
  
  protected static String extractNumber(Object value, boolean expectedInteger) {
    if(value == null) return "0";
    if(value instanceof Number) {
      return value.toString();
    }
    String sValue = value.toString();
    if(sValue == null || sValue.length() == 0) {
      return "0";
    }
    sValue = sValue.trim();
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < sValue.length(); i++) {
      char c = sValue.charAt(i);
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
  class FInfo implements Serializable
  {
    private static final long serialVersionUID = 1937128145502235421L;
    
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
